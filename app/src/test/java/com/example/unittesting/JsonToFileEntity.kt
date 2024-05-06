package com.example.unittesting

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

const val FILE_TYPE_WE_BOX_ON_DEVICE_DIRECTORY = 20 // Available directory on WeBox storage
const val FILE_TYPE_WE_BOX_ON_DEVICE_FILE = 0

class JsonToFileEntity {

    private val json: String = jsonVariable

    @Before
    fun setup() {
        MockKAnnotations.init(this)

    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Basic Test`() {
        val gson = Gson()
        val listType = object : TypeToken<List<FileEntityX>>() {}.type
        val fileEntities: List<FileEntityX> = gson.fromJson(jsonVariable, listType)

        val workingFileEntities: MutableList<FileEntityX> = mutableListOf()

        fileEntities.forEach { fileEntityX ->
            if (fileEntityX.folderPath != null) {

                val existingSubFiles = fileEntityX.folderPath?.split("/")
                val newSubFiles = mutableListOf<FileEntityX>()

                var root: FileEntityX? = null

                existingSubFiles?.forEachIndexed { index, folderName ->
                    if (index == 0) {
                        if (!workingFileEntities.map { it.fName }.contains(folderName)) {

                            root = FileEntityX(
                                fName = folderName,
                                fileType = FILE_TYPE_WE_BOX_ON_DEVICE_DIRECTORY
                            ).also {
                                workingFileEntities.add(it)
                            }
                        }
                    } else if (!newSubFiles.map { it.fName }.contains(folderName)) {
                        newSubFiles.add(
                            FileEntityX(
                                fName = folderName,
                                fileType = FILE_TYPE_WE_BOX_ON_DEVICE_DIRECTORY
                            )
                        )
                        root?.sub_files = newSubFiles
                    }
                }
            }
        }
        Assert.assertEquals(workingFileEntities.map { it.fName }, listOf("SuperHero"))
    }


    @Test
    fun x() {
        val result = createFileEntity("SuperHero/SpiderMan/Colors/Red.png")
        println(result)
    }

    private fun createFileEntity(folderPath: String): FileEntityX {
        val folders = folderPath.split("/")
        return createFileEntityRecursive(folders, 0)
    }

    private fun createFileEntityRecursive(folders: List<String>, index: Int): FileEntityX {
        if (index >= folders.size - 1) {
            // Base case: Reached the last folder or file in the path
            return FileEntityX(
                fName = folders.last(),
                fileType = if (index == folders.size - 1) {
                    // If this is the last item, it's a file
                    FILE_TYPE_WE_BOX_ON_DEVICE_FILE
                } else {
                    // If not, it's a directory
                    FILE_TYPE_WE_BOX_ON_DEVICE_DIRECTORY
                }
            )
        }

        val currentFolder = FileEntityX(
            fName = folders[index],
            fileType = FILE_TYPE_WE_BOX_ON_DEVICE_DIRECTORY
        )
        currentFolder.sub_files = listOf(createFileEntityRecursive(folders, index + 1))

        return currentFolder
    }



}
