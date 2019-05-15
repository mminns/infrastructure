package com.atlassian.performance.tools.infrastructure.mock

import com.atlassian.performance.tools.ssh.api.DetachedProcess
import com.atlassian.performance.tools.ssh.api.Ssh
import com.atlassian.performance.tools.ssh.api.SshConnection
import com.atlassian.performance.tools.ssh.api.SshHost
import org.apache.logging.log4j.Level
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration

class MockSshConnection : SshConnection {


    val executionAudit : MutableList<String> = mutableListOf()

    override fun execute(cmd: String, timeout: Duration, stdout: Level, stderr: Level): SshConnection.SshResult {
        executionAudit.add(cmd)
        return SshConnection.SshResult(0, "", "")
    }

    override fun safeExecute(cmd: String, timeout: Duration, stdout: Level, stderr: Level): SshConnection.SshResult {
        executionAudit.add(cmd)
        return SshConnection.SshResult(0, "", "")
    }

    internal fun getExecutionAudit(): List<String> {
        return executionAudit
    }

    override fun close() {
        throw NotImplementedError("Unexpected call")
    }

    override fun download(remoteSource: String, localDestination: Path) {
        throw NotImplementedError("Unexpected call")
    }

    override fun startProcess(cmd: String): DetachedProcess {
        throw NotImplementedError("Unexpected call")
    }

    override fun stopProcess(process: DetachedProcess) {
        throw NotImplementedError("Unexpected call")
    }

    override fun upload(localSource: File, remoteDestination: String) {
        throw NotImplementedError("Unexpected call")
    }
}