package com.example.unittesting

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class LogicValidationTest {

    private val logicValidation: LogicValidation = LogicValidation()

    @io.mockk.impl.annotations.MockK
    private lateinit var myFile: MyFile

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `bard test case`() {
        val items = listOf(
            FileEntity(fPath = "/path/to/file1.txt", fSize = 100),
            FileEntity(fPath = "/path/to/file2.txt", fSize = 200),
            FileEntity(fPath = "/path/to/file3.wbx", fSize = 300),
        )

        // When
        val actualResult = logicValidation.getItemsWithMaxSize(items)

        val expectedResult: List<FileEntity> = listOf(
            FileEntity(fPath = "/path/to/file1.txt", fSize = 100),
            FileEntity(fPath = "/path/to/file2.txt", fSize = 200),
            FileEntity(fPath = "/path/to/file3.wbx", fSize = 300),
        )

        // Then
        assertEquals(
            expectedResult,
            actualResult
        )

    }

    @Test
    fun `new edge case`() {
        // Given
        val inputList: List<FileEntity> = listOf(
            FileEntity("asdsfsdf/dsf/sdf/sd/fsd/abc.pptx.pdf", 200),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/abc.pdf", 300),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/xyz.pdf.WBX", 300),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/xyz.pdf", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/www.pdf.pptx.xml", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/www.pdf", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/gyz.pdf.zip", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/gyz.pdf.pptx.zip", 100)
        )

        val expectedResult: List<FileEntity> = listOf(
            FileEntity("asdsfsdf/dsf/sdf/sd/fsd/abc.pptx.pdf", 200),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/abc.pdf", 300),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/xyz.pdf.WBX", 300),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/www.pdf.pptx.xml", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/www.pdf", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/gyz.pdf.zip", 100),
            FileEntity("dsdf/dsf/dsf/ds/fds/fds/f/ds/gyz.pdf.pptx.zip", 100)
        )

        // When
        val actualResult = logicValidation.getItemsWithMaxSize(inputList)

        // Then
        assertEquals(
            expectedResult,
            actualResult
        )
    }

    @Test
    fun `should choose highest value from each group with same name ignoring wbx`() {

        // Given
        val inputList: List<FileEntity> = listOf(

            // Case where .wbx file is more
            FileEntity("sdfdsf/dfdsfdsf/sdfsdfsd/aaa.pdf", 1000),
            FileEntity("sdfdsf/sdfdsf/dsf/dsfsd/fsd/fsd/fsd/aaa.pdf.wbx", 2000),

            // Case where plain file is more
            FileEntity("ewrwer/erer/erewrew/rew/rewrwer/ewr/ewrewrwe/bbb.doc", 100),
            FileEntity("ergrg/rgrtgrtg/trg/rtgrt/gr/tgrt/g/rtgrtgrtg/rt/bbb.doc.wbx", 20),

            // Case where plain file is more
            FileEntity("fefer/fer/fer/fre/fr/frefer/fer/frefref/refref/ccc.mov", 2000),
            FileEntity("dfgfdg/dfgdfgdfg/dfgd/fgdfgdf/gdf/gdfgdf/g/ccc.mov.wbx", 1000),
        )

        val expectedResult: List<FileEntity> = listOf(
            FileEntity("sdfdsf/sdfdsf/dsf/dsfsd/fsd/fsd/fsd/aaa.pdf.wbx", 2000),
            FileEntity("ewrwer/erer/erewrew/rew/rewrwer/ewr/ewrewrwe/bbb.doc", 100),
            FileEntity("fefer/fer/fer/fre/fr/frefer/fer/frefref/refref/ccc.mov", 2000)
        )

        // When
        val actualResult = logicValidation.getItemsWithMaxSize(inputList)

        // Then
        assertEquals(
            expectedResult,
            actualResult
        )
    }

    @Test
    fun `beginner test`() {
        // Given

        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc.pdf"
        val expectedResult = "abc.pdf"

        every { myFile.isFileExist(any()) } returns false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `secondary test`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(1).pdf"
        val expectedResult = "abc(2).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `third test`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(11).pdf"
        val expectedResult = "abc(12).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `fourth test`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(100).pdf"
        val expectedResult = "abc(101).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `fifth test`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(100)(1).pdf"
        val expectedResult = "abc(100)(2).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `sixth test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(def).pdf"
        val expectedResult = "abc(def)(1).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `seventh test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(1)(2).pdf"
        val expectedResult = "abc(1)(3).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `eighth test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/123abc.pdf"
        val expectedResult = "123abc(1).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `9 test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abcdefg.pdf"
        val expectedResult = "abcdefg(1).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `10 test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc1.pdf"
        val expectedResult = "abc1(1).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `11 test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(1)def.pdf"
        val expectedResult = "abc(1)def(1).pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `12 test case`() {
        // Given
        val filePath = "sfsdf/sfrfref/erf/file(1).txt"
        val expectedResult = "file(2).txt"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `13 test case`() {
        // Given
        val filePath = "sfsdf/sfrfref/erf/file(1(2)).txt"
        val expectedResult = "file(1(2))(1).txt"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `14 test case`() {
        // Given
        val filePath = "sfsdf/sfrfref/erf/file(1) (2).txt"
        val expectedResult = "file(1) (3).txt"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `15 test case`() {
        // Given
        val filePath = "sfsdf/sfrfref/erf/file(1).txt.pdf"
        val expectedResult = "file(2).txt.pdf"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `16 test case`() {
        // Given
        val filePath = "sfsdf/sfrfref/erf/file.txt."
        val expectedResult = "file.txt(1)."

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `17 test case`() {
        // Given
        val filePath = "sfsdf/sfrfref/erf//file<>.txt"
        val expectedResult = "file<>(1).txt"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.getUniqueFileName(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `first test for pptxpdf`() {
        // Given
        val input = "abc.pptx.pdf"
        val expectedOutput = "abc.pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `second test for pptxpdf`() {
        // Given
        val input = "abc.pptx"
        val expectedOutput = "abc.pptx"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `third test for pptxpdf`() {
        // Given
        val input = "abc.pdf"
        val expectedOutput = "abc.pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `fourth test for pptxpdf`() {
        // Given
        val input = "abc.pptx.pdf.sdsds.pptx.pdf"
        val expectedOutput = "abc.pptx.pdf.sdsds.pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `fifth test for pptxpdf`() {
        // Given
        val input = "abc.xml"
        val expectedOutput = "abc.xml"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `edge case test for pptxpdf`() {
        // Given
        val input = ".pptx.pdf"
        val expectedOutput = ".pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `sixth case test for pptxpdf`() {
        // Given
        val input = "abc.pptx.pdf.xyz.pptx.pdf"
        val expectedOutput = "abc.pptx.pdf.xyz.pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `seventh case test for pptxpdf`() {
        // Given
        val input = "abc.pptx.pdf.xml"
        val expectedOutput = "abc.pptx.pdf.xml"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `eighth case test for pptxpdf`() {
        // Given
        val input = " abc.pptx.pdf "
        val expectedOutput = "abc.pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }

    @Test
    fun `ninth case test for pptxpdf`() {
        // Given
        val input = "abc.PPTX.PDF"
        val expectedOutput = "abc.pdf"

        // When
        val actualResult = logicValidation.replacePPTXPDF(input)

        // Then
        assertEquals(expectedOutput, actualResult)
    }
}