package com.atlassian.performance.tools.infrastructure.api.dynatrace

import com.atlassian.performance.tools.infrastructure.api.process.RemoteMonitoringProcess
import com.atlassian.performance.tools.infrastructure.api.profiler.Profiler
import com.atlassian.performance.tools.ssh.api.SshConnection
import java.net.URI

/**
 *  Dynatrace Agent for Linux. See https://www.dynatrace.com/support/help/technology-support/operating-systems/linux/installation/install-oneagent-on-linux/
 */
class DynatraceAgentForLinux(
    val dynatraceHost: URI,
    val apiToken: String,
    val agentVersion: String)  : Profiler
{

    override fun install(ssh: SshConnection) {
        ssh.execute("wget  -O Dynatrace-OneAgent-Linux-${agentVersion}.sh \"${dynatraceHost}/api/v1/deployment/installer/agent/unix/default/latest?Api-Token=${apiToken}&arch=x86&flavor=default\"")
        ssh.execute("wget https://ca.dynatrace.com/dt-root.cert.pem ; ( echo 'Content-Type: multipart/signed; protocol=\"application/x-pkcs7-signature\"; micalg=\"sha-256\"; boundary=\"--SIGNED-INSTALLER\"'; echo ; echo ; echo '----SIGNED-INSTALLER' ; cat Dynatrace-OneAgent-Linux-${agentVersion}.sh ) | openssl cms -verify -CAfile dt-root.cert.pem > /dev/null")

        // startup before Java applications start
        ssh.execute("sudo sh Dynatrace-OneAgent-Linux-${agentVersion}.sh  APP_LOG_CONTENT_ACCESS=1 INFRA_ONLY=0")
    }

    override fun start(
        ssh: SshConnection,
        pid: Int
    ): RemoteMonitoringProcess {
        // do nothing the agent was started during installation
        return ProfilerProcess(pid)
    }

    private class ProfilerProcess(private val pid: Int) : RemoteMonitoringProcess {

        override fun stop(ssh: SshConnection) {
            // do nothing, there are no local results so no need to explicitly stop/flush the agent, rely on it being shutdown when the host shuts down
        }

        override fun getResultPath(): String {
            // no local results
            return "";
        }
    }
}