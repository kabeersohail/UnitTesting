package com.example.unittesting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


const val WEBOX_EXTENSION = ".WBX"
const val NUMBER_ZERO = 0
const val NUMBER_FOUR = 4

class LogicValidation {

    @VisibleForTesting
    internal fun getItemsWithMaxSize(items: List<FileEntity>): MutableList<FileEntity> {
        val map = mutableMapOf<String, MutableList<FileEntity>>()

        for (item in items) {
            val nameWithAllExtensions = item.fPath.substringAfterLast(File.separator)

            if(nameWithAllExtensions.isNotBlank()) {
                val key = if(nameWithAllExtensions.endsWith(WEBOX_EXTENSION, ignoreCase = true)) {
                    nameWithAllExtensions.substring(NUMBER_ZERO, nameWithAllExtensions.length - NUMBER_FOUR)
                } else {
                    nameWithAllExtensions
                }

                val list = map.getOrPut(key) { mutableListOf() }
                list.add(item)
            }
        }

        return map.values.mapNotNull { itemsX ->
            itemsX.maxByOrNull { it.fSize }
        }.toMutableList()
    }

    fun replacePPTXPDF(fileName: String): String {

        if(!fileName.endsWith(".pptx.pdf", ignoreCase = true)) {
            return fileName
        }

        return if(fileName.toLowerCase().endsWith(".pptx.pdf".toLowerCase())) {
            fileName.substring(0, fileName.length - ".pptx.pdf".length) + ".pdf"
        } else {
            fileName
        }

    }





//    fun uniquify(path: String, myFile: MyFile): String {
//        val file = File(path)
//        val filename = file.nameWithoutExtension.replace(Regex("\\(\\d+\\)$"), "")
//        val extension = file.extension
//        var counter = 1
//
//        var newPath = path
//        while (myFile.isFileExist(newPath)) {
//            counter++
//            newPath = "$filename($counter).$extension"
//        }
//
//        return filename
//    }

//        fun uniquify2(path: String, myFile: MyFile): String {
//            val file = File(path)
//            val filename = file.nameWithoutExtension.replace(Regex("\\(\\d+\\)$"), "")
//            val extension = file.extension
//            val regex = Regex("\\((\\d+)\\)[^()]*\$")
//            val matchResult = regex.find(file.nameWithoutExtension)
//            val counter = matchResult?.groupValues?.get(1)?.toInt() ?: 1
//
//            var newPath = path
//            var newFilename: String
//            var newCounter = counter
//            while (myFile.isFileExist(newPath)) {
//                newCounter++
//                newFilename = "$filename($newCounter)"
//                newPath = "$newFilename.$extension"
//            }
//
//            return newPath
//        }

//    fun uniquify2(path: String, myFile: MyFile): String {
//        val file = File(path)
//        val regex = Regex("\\((\\d+)\\)(?=\\.[^.]+$)|(?<=\\.[^.])\\((\\d+)\\)$")
//        val filename = regex.replace(file.nameWithoutExtension) { matchResult ->
//            val counter = matchResult.groupValues[1].toInt() + 1
//            "(${counter})"
//        }
//        val extension = file.extension
//
//        var newPath = path
//        var newFilename = filename
//        var newCounter = 1
//        while (myFile.isFileExist(newPath)) {
//            newCounter++
//            newFilename = regex.replace(file.nameWithoutExtension) { matchResult ->
//                val counter = matchResult.groupValues[1].toInt() + newCounter
//                "(${counter})"
//            }
//            newPath = "${File(file.parent, newFilename).path}.$extension"
//        }
//
//        return if (newPath != path) newPath else "${File(file.parent, filename).path}.$extension"
//    }


    @RequiresApi(Build.VERSION_CODES.O)
//    fun uniquify3(path: String, myFile: MyFile): String {
//        val file = File(path)
//        val basename = FilenameUtils.getBaseName(file.name)
//        val extension = FilenameUtils.getExtension(file.name)
//        val regex = Regex("\\((\\d+)\\)$")
//        val matchResult = regex.find(basename)
//        val counter = matchResult?.groupValues?.get(1)?.toInt() ?: 0
//
//        var newPath = path
//        var newBasename: String
//        var newCounter = counter
//        while (myFile.isFileExist(newPath)) {
//            newCounter++
//            newBasename = "$basename($newCounter)"
//            newPath = FilenameUtils.concat(file.parent, "$newBasename.$extension")
//        }
//
//        return if (newPath != path) newPath else "$basename.$extension"
//    }

    fun getUniqueFileName(fileName: String, myFile: MyFile): String {
        val path = Paths.get(fileName)
        if (!myFile.isFileExist(path)) {
            return fileName
        }
        val fileNameWithoutExtension = path.fileName.toString().substringBeforeLast(".")
        val fileExtension = path.fileName.toString().substringAfterLast(".")
        var index = 1
        var uniqueFileName: String
        do {
            uniqueFileName = "${fileNameWithoutExtension}($index).$fileExtension"
            index++
        } while (myFile.isFileExist(Paths.get(uniqueFileName)))
        return uniqueFileName
    }


}

class MyFile {
    //    fun isFileExist(newFilePath: String) = File(newFilePath).exists()
    @RequiresApi(Build.VERSION_CODES.O)
    fun isFileExist(path: Path?) = Files.exists(path)

}

data class FileEntity(
    val fPath: String,
    val fSize: Long
)