package com.example.unittesting

import androidx.annotation.VisibleForTesting
import java.io.File
import java.util.*

class LogicValidation {

    @VisibleForTesting
    internal fun getItemsWithMaxSize(items: List<FileEntity>): MutableList<FileEntity> {
        val map = mutableMapOf<String, MutableList<FileEntity>>()

        for (item in items) {
            val name = item.fPath.substringAfterLast("/").substringBeforeLast(".").toLowerCase(
                Locale.ROOT)
            if (name.isNotBlank()) {
                val key = name.substringBeforeLast(".")
                val list = map.getOrPut(key) { mutableListOf() }
                list.add(item)
            }
        }

        return map.values.mapNotNull { itemsX ->
            itemsX.maxByOrNull { it.fSize }
        }.toMutableList()
    }

    fun uniquify(path: String, myFile: MyFile): String {
        val file = File(path)
        val filename = file.nameWithoutExtension.replace(Regex("\\(\\d+\\)$"), "")
        val extension = file.extension
        var counter = 1

        var newPath = path
        while (myFile.isFileExist(newPath)) {
            counter++
            newPath = "$filename($counter).$extension"
        }

        return filename
    }

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

    fun uniquify2(path: String, myFile: MyFile): String {
        val file = File(path)
        val filename = file.nameWithoutExtension.replace(Regex("\\(\\d+\\)$"), "")
        val extension = file.extension
        val regex = Regex("\\((\\d+)\\)(\\.[^.]+)?\$")
        val matchResult = regex.find(file.nameWithoutExtension)
        val counter = matchResult?.groupValues?.get(1)?.toInt() ?: 0

        var newPath = path
        var newFilename: String
        var newCounter = counter
        while (myFile.isFileExist(newPath)) {
            newCounter++
            newFilename = "$filename($newCounter)"
            newPath = "$newFilename.$extension"
        }

        return if (newPath != path) newPath else "$filename.$extension"
    }




}

class MyFile {
    fun isFileExist(newFilePath: String) = File(newFilePath).exists()
}

data class FileEntity(
    val fPath: String,
    val fSize: Long
)