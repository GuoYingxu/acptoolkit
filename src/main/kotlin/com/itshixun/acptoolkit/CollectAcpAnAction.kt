package com.itshixun.acptoolkit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class CollectAcpAnAction:AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        project?.let {
            val acpService = it.getService(AcpService::class.java)
            acpService.collectAcpDefinition()
        }
    }
}