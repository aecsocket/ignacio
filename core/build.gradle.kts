dependencies {
    compileOnly(libs.kotlinReflect)
    compileOnly(libs.kotlinxCoroutines)

    compileOnly(libs.configurateCore)

    testImplementation(platform("org.junit:junit-bom:5.9.0"))
}
