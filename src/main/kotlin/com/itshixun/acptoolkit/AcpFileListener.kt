package com.itshixun.acptoolkit
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.*

class AcpFileListener(private val project: Project) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        super.after(events)
        println("after")
        for (event in events) {
            if (event is VFileContentChangeEvent) {
                println(event)
                println("acp file event")
                ApplicationManager.getApplication().invokeLater {
                    val psiFile = PsiManager.getInstance(this.project).findFile(event.file)
                    psiFile?.accept(object : JavaRecursiveElementVisitor() {
                        override fun visitIdentifier(identifier: PsiIdentifier) {
                            if (identifier.text == "PermissionDefiner") {
                                println("doDefinePermissions")
                                if (identifier.parent.parent.parent is PsiClass) {
                                    AcpUtils.collectAcpFromClass(identifier.parent.parent.parent as PsiClass)
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}