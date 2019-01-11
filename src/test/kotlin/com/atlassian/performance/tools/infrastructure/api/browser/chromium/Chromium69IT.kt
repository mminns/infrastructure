package com.atlassian.performance.tools.infrastructure.api.browser.chromium

import com.atlassian.performance.tools.infrastructure.docker.SshUbuntuContainer
import com.atlassian.performance.tools.ssh.api.SshConnection
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class Chromium69IT {

    @Test
    fun shouldInstallBrowser() {
        SshUbuntuContainer().run { ssh ->
            val wasInstalledBefore = isChromiumInstalled(ssh)

            Chromium69().install(ssh)

            val isInstalledAfter = isChromiumInstalled(ssh)

            Assert.assertThat(wasInstalledBefore, Matchers.`is`(false))
            Assert.assertThat(isInstalledAfter, Matchers.`is`(true))
        }
    }

    @Test
    fun shouldRecoverFromPageLoadTimeout() {
        PageLoadTimeoutRecoveryTest().run(Chromium69())
    }

    private fun isChromiumInstalled(ssh: SshConnection): Boolean {
        val result = ssh
            .safeExecute("ls -lh /usr/bin/chrome")
        return result.isSuccessful()
            .and(
                result
                    .output
                    .contains("/root/chrome-linux/chrome")
            )
    }
}