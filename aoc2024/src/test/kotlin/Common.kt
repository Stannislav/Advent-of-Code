import java.io.FileNotFoundException
import java.io.InputStream

fun streamInput(path: String): InputStream {
    return object {}.javaClass.classLoader.getResourceAsStream(path) ?: throw FileNotFoundException("Resource $path not found")
}
