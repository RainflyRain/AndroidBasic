package com.friean.build

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        println 'hello plugin build'+project.name
    }
}