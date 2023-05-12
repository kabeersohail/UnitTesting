package com.example.unittesting

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.unmockkAll
import org.junit.After
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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

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
        val actualResult = logicValidation.uniquify2(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `11 test case`() {
        // Given
        val filePath = "sdfdsfsd/fdsfdsf/ddssd/fsdf/abc(1)def.pdf"
        val expectedResult = "abc(1)def(1).def"

        every { myFile.isFileExist(any()) } returns true andThen false

        // When
        val actualResult = logicValidation.uniquify2(filePath, myFile)

        // Then
        assertEquals(expectedResult, actualResult)
    }

}