/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle

import org.jetbrains.kotlin.ide.konan.gradle.KotlinGradleNativeMultiplatformModuleBuilder
import org.jetbrains.kotlin.idea.configuration.KotlinGradleMobileMultiplatformModuleBuilder
import org.jetbrains.kotlin.idea.configuration.KotlinGradleMobileSharedMultiplatformModuleBuilder
import org.jetbrains.kotlin.idea.configuration.KotlinGradleSharedMultiplatformModuleBuilder
import org.jetbrains.kotlin.idea.configuration.KotlinGradleWebMultiplatformModuleBuilder
import org.junit.Test

class GradleMultiplatformWizardTest : AbstractGradleMultiplatformWizardTest() {
    @Test
    fun testMobile() {
        // TODO: add import & tests here when we will be able to locate Android SDK automatically (see KT-27635)
        testImportFromBuilder(KotlinGradleMobileMultiplatformModuleBuilder(), performImport = false)
    }

    @Test
    fun testMobileShared() {
        testImportFromBuilder(
            KotlinGradleMobileSharedMultiplatformModuleBuilder(),
            "SampleTests", "SampleTestsJVM", "SampleTestsNative", metadataInside = true
        )
    }

    @Test
    fun testNative() {
        // TODO: add test run here (probably after fix of KT-27599)
        testImportFromBuilder(KotlinGradleNativeMultiplatformModuleBuilder())
    }

    @Test
    fun testShared() {
        testImportFromBuilder(
            KotlinGradleSharedMultiplatformModuleBuilder(),
            "SampleTests", "SampleTestsJVM", "SampleTestsNative", metadataInside = true
        )
    }

    @Test
    fun testWeb() {
        testImportFromBuilder(KotlinGradleWebMultiplatformModuleBuilder(), "SampleTests", "SampleTestsJVM")
    }
}