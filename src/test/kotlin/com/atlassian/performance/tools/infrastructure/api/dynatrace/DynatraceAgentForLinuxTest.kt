package com.atlassian.performance.tools.infrastructure.api.dynatrace

import com.atlassian.performance.tools.infrastructure.mock.MockSshConnection
import org.assertj.core.api.Assertions
import org.junit.Test
import java.net.URI

class DynatraceAgentForLinuxTest {

    var apiHost = URI("https://example.live.dynatrace.com:8180")
    val apiToken = "11223344556677889900"
    val agentVersion = "15.5.2019"

    @Test
    fun installShouldDownloadAndInstallBasedOnRuntimeData() {


        val ssh = MockSshConnection()

        DynatraceAgentForLinux(apiHost, apiToken, agentVersion).install(ssh)

        Assertions.assertThat(ssh.getExecutionAudit()).containsExactly(
            "wget  -O Dynatrace-OneAgent-Linux-${agentVersion}.sh \"${apiHost}/api/v1/deployment/installer/agent/unix/default/latest?Api-Token=${apiToken}&arch=x86&flavor=default\"",
            "wget https://ca.dynatrace.com/dt-root.cert.pem ; ( echo 'Content-Type: multipart/signed; protocol=\"application/x-pkcs7-signature\"; micalg=\"sha-256\"; boundary=\"--SIGNED-INSTALLER\"'; echo ; echo ; echo '----SIGNED-INSTALLER' ; cat Dynatrace-OneAgent-Linux-${agentVersion}.sh ) | openssl cms -verify -CAfile dt-root.cert.pem > /dev/null",
            "sudo sh Dynatrace-OneAgent-Linux-${agentVersion}.sh  APP_LOG_CONTENT_ACCESS=1 INFRA_ONLY=0")
    }

    @Test
    fun startShouldDoNothingButGivesValidRemoteMonitoringProcess() {
        val ssh = MockSshConnection()
        val pid = 999

        val process = DynatraceAgentForLinux(apiHost, apiToken, agentVersion).start(ssh, pid)

        Assertions.assertThat(ssh.getExecutionAudit()).isEmpty()

        Assertions.assertThat(process).isNotNull

        process.stop(ssh)
        Assertions.assertThat(ssh.getExecutionAudit()).isEmpty()

        val resultPath = process.getResultPath()
        Assertions.assertThat(resultPath).isEmpty()
    }
}