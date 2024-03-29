package com.steamstreet.graphkt.generator

import graphql.schema.idl.SchemaParser
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.*
import org.gradle.api.tasks.Optional
import java.io.File
import java.util.*

/**
 * The task to generate code.
 */
open class GraphQLCodeGenerator : DefaultTask() {
    @InputFile
    fun getSchema(): File {
        return File(project.graphQL().schema)
    }

    @InputFile
    @Optional
    fun getProperties(): File? {
        return File(getSchema().parentFile, "${getSchema().nameWithoutExtension}.properties").takeIf { it.exists() }
    }

    @InputFiles
    fun getSchemaFiles(): List<File> {
        val schemaFile = getSchema()
        return schemaFile.parentFile.listFiles { _, name ->
            name != schemaFile.name && name.endsWith(".graphql")
        }?.toList() ?: emptyList()
    }

    @OutputDirectory
    fun getGeneratedOutputDir(): File {
        return File(project.buildDir, "graphql/generated")
    }

    @OutputDirectory
    fun getServerGeneratedOutputDir(): File {
        return File(project.buildDir, "graphql/server/generated")
    }

    @TaskAction
    fun doAction() {
        val schemaFile = getSchema()
        if (!schemaFile.exists()) {
            throw GradleException("You must specify a path to the schema file")
        }

        val parser = SchemaParser()
        val schema = parser.parse(schemaFile)

        val schemaFiles = getSchemaFiles()
        schemaFiles.forEach {
            val nested = parser.parse(it)
            schema.merge(nested)
        }

        val outputDir = getGeneratedOutputDir()
        outputDir.mkdirs()

        getServerGeneratedOutputDir().mkdirs()

        val properties = Properties().also { properties ->
            getProperties()?.inputStream()?.use {
                properties.load(it)
            }
        }

        val configuration = project.graphQL()
        DataTypesGenerator(schema, configuration.basePackage, properties, outputDir).execute()
        if (configuration.generateClient) {
            QueryGenerator(schema, configuration.basePackage, properties, outputDir).execute()
            ResponseParserGenerator(schema, configuration.basePackage, properties, outputDir).execute()
        }
        if (configuration.generateServer) {
            ServerInterfacesGenerator(schema, configuration.basePackage, properties, outputDir).execute()
            ServerMappingGenerator(schema, configuration.basePackage, properties, outputDir).execute()
        }
    }
}