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
    val ssh: Ssh = Ssh(SshHost("","", Paths.get("")))

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun download(remoteSource: String, localDestination: Path) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startProcess(cmd: String): DetachedProcess {
        // I don't think this will work...
        return ssh.newConnection().startProcess(cmd)
    }

    override fun stopProcess(process: DetachedProcess) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun upload(localSource: File, remoteDestination: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}