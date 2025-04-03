package com.matthewprenger.cursegradle

import com.google.common.base.Strings
import com.matthewprenger.cursegradle.jsonresponse.CurseError
import com.matthewprenger.cursegradle.jsonresponse.UploadResponse
import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpResponse
import org.apache.hc.core5.http.message.StatusLine
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.apache.http.client.config.CookieSpecs

//import org.gradle.internal.impldep.org.apache.http.HttpResponse

class CurseUploadTask extends DefaultTask {

    private static final Logger log = Logging.getLogger(CurseUploadTask)

    @Input
    String apiKey

    @Input
    String projectId

    @Input
    CurseArtifact mainArtifact

    @Input
    Collection<CurseArtifact> additionalArtifacts

    @TaskAction
    run() {

        Util.check(!Strings.isNullOrEmpty(apiKey), "CurseForge Project $projectId does not have an apiKey configured")

        mainArtifact.resolve(project)

        CurseVersions.initialize(apiKey)
        mainArtifact.gameVersions = CurseVersions.resolveGameVersion(mainArtifact.gameVersionStrings)

        final String json = Util.gson.toJson(mainArtifact)
        int mainID = uploadFile(json, (File) mainArtifact.artifact)
        mainArtifact.fileID = mainID

        additionalArtifacts.each { artifact ->
            artifact.resolve(project)
            artifact.parentFileID = mainID
            final String childJson = Util.gson.toJson(artifact)
            artifact.fileID = uploadFile(childJson, (File) artifact.artifact)
        }
    }

    int uploadFile(String json, File file) throws IOException, URISyntaxException {

        int fileID
        final String uploadUrl = String.format(CurseGradlePlugin.UPLOAD_URL, projectId)
        log.info("Uploading file: {} to url: {} with json: {}", file, uploadUrl, json)

        HttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build()

        HttpPost post = new HttpPost(new URI(uploadUrl))

        post.addHeader('X-Api-Token', apiKey)
        post.setEntity(MultipartEntityBuilder.create()
                .addTextBody('metadata', json, ContentType.APPLICATION_JSON)
                .addBinaryBody('file', file)
                .build())

        if (project.extensions.getByType(CurseExtension).curseGradleOptions.debug) {
            logger.lifecycle("DEBUG: File: $file  Json: $json")
            return 0
        }

        HttpResponse response = client.execute(post)
        StatusLine statusLine = new StatusLine(response)

        if (statusLine.statusCode == 200) {
            InputStreamReader reader = new InputStreamReader(response.entity.content)
            UploadResponse curseResponse = Util.gson.fromJson(reader, UploadResponse)
            reader.close()
            fileID = curseResponse.id
        } else {
            if (response.getFirstHeader('content-type').value.contains('json')) {
                InputStreamReader reader = new InputStreamReader(response.entity.content)
                CurseError error = Util.gson.fromJson(reader, CurseError)
                reader.close()
                throw new RuntimeException("[CurseForge ${projectId}] Error Code ${error.errorCode}: ${error.errorMessage}")
            } else {
                throw new RuntimeException("[CurseForge ${projectId}] HTTP Error Code $statusLine.statusCode: $statusLine.reasonPhrase")
            }
        }

        log.lifecycle "Uploaded {} to CurseForge Project: {}, with ID: {}", file, projectId, fileID
        return fileID
    }
}
