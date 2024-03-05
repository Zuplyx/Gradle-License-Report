/*
 * Copyright 2024 Patrick Schmitt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jk1.license.reader

import com.github.jk1.license.LicenseReportExtension
import com.github.jk1.license.ModuleData
import org.gradle.api.Project

class FileDependencyReader {

    private LicenseReportExtension config
    private PomReader pomReader
    private ManifestReader manifestReader
    private LicenseFilesReader filesReader

    FileDependencyReader(LicenseReportExtension config) {
        this.config = config
        this.pomReader = new PomReader(config)
        this.manifestReader = new ManifestReader(config)
        this.filesReader = new LicenseFilesReader(config)
    }

    ModuleData read(Project project, File dependency){
        ModuleData moduleData = new ModuleData(null, dependency.name, null)
        def pom = pomReader.readPomData(project, dependency)
        def manifest = manifestReader.readManifestData(dependency)
        def licenseFile = filesReader.read(dependency)

        if (pom) moduleData.poms << pom
        if (manifest) moduleData.manifests << manifest
        if (licenseFile) moduleData.licenseFiles << licenseFile
        return moduleData
    }
}
