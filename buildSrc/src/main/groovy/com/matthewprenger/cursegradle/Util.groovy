package com.matthewprenger.cursegradle

import com.google.gson.Gson
import com.matthewprenger.cursegradle.jsonresponse.CurseError
import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.core5.http.HttpResponse
import org.apache.hc.core5.http.message.StatusLine
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.bundling.AbstractArchiveTask

import java.nio.charset.StandardCharsets

import static com.google.common.base.Preconditions.checkNotNull

class Util {

    private static final Logger log = Logging.getLogger(Util)

    static final Gson gson = new Gson()

    /**
     * Resolve an object into a file
     *
     * @param project The project
     * @param obj The object to resolve
     * @return A file instance
     */
    static File resolveFile(Project project, Object obj) {
        if (obj == null) {
            throw new NullPointerException("Null path")
        }
        if (obj instanceof File) {
            return (File) obj
        }
        if (obj instanceof AbstractArchiveTask) {
            return ((AbstractArchiveTask) obj).getArchiveFile().get().getAsFile()
        }
        return project.file(obj)
    }

    /**
     * Resolve an object into a String. If a file is passed, it will be read and it's contents returned
     *
     * @param obj The object to resolve
     * @return A string
     */
    static String resolveString(Object obj) {
        checkNotNull(obj)

        while (obj instanceof Closure) {
            obj = ((Closure) obj).call()
        }

        if (obj instanceof String) {
            return (String) obj
        }
        if (obj instanceof File) {
            String data = new String(((File) obj).getText('UTF-8').getBytes('UTF-8'))
            return data
        }

        if (obj instanceof AbstractArchiveTask) {
            String data = new String(
                    (((AbstractArchiveTask) obj).getArchiveFile().get().getAsFile())
                            .getText('UTF-8')
                            .getBytes('UTF-8'))
            return data
        }

        return obj.toString()
    }

    /**
     * Issue an HTTP GET to a CurseForge API URL
     *
     * @param apiKey The apiKey to use for connecting
     * @param url The url
     * @return The data
     */
    static String httpGet(String apiKey, String url) {
        log.debug "HTTP GET to URL: $url"

        HttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec("ignoreCookies").build()).build()

        HttpGet get = new HttpGet(new URI(url))
        get.setHeader('X-Api-Token', apiKey)

        HttpResponse response = client.execute(get)
        StatusLine statusLine = new StatusLine(response)

        int statusCode = statusLine.statusCode

        if (statusCode == 200) {
            byte[] data = response.entity.content.bytes
            return new String(data, StandardCharsets.UTF_8)
        } else {
            if (response.getFirstHeader('content-type').value.contains('json')) {
                InputStreamReader reader = new InputStreamReader(response.entity.content)
                CurseError error = gson.fromJson(reader, CurseError)
                reader.close()
                throw new RuntimeException("[CurseForge] Error Code ${error.errorCode}: ${error.errorMessage}")
            } else {
                throw new RuntimeException("[CurseForge] HTTP Error Code $statusLine.statusCode: $statusLine.reasonPhrase")
            }
        }
    }

    /**
     * Check if a condition is true, and raise an exception if not
     *
     * @param condition The condition
     * @param message The message to display
     */
    static void check(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message)
        }
    }
}
