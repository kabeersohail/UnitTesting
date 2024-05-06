package com.example.unittesting

data class FileEntityX(
    var uniqueID: Long = 0,
    var fName: String? = null,
    var fSize: Long = 0,
    var fPath: String? = null,
    var fCheckSum: String? = null,
    var fExtension: String? = null,
    var onDeviceFilePath: String? = null,
    var timestamp: Long = 0,
    var isDirectory: Boolean = false,
    var sub_files: List<FileEntityX>? = null,
    var zipType: Int = 0,
    var reExtractZip: Boolean = false,
    var fileType: Int = 0,
    var label: String? = null,
    var s3UniqueKey: String? = null,
    var s3BucketName: String? = null,
    var folderPath: String? = null
)
