package com.itshixun.acptoolkit

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.*

class AcpFileListener(val project:Project) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        super.after(events)
        println("after")
        for (event in events) {
            println(event.path)
            if(event is VFileContentChangeEvent) {
                println("acp file event")
                val psiFile  = PsiManager.getInstance(this.project).findFile(event.file)
                psiFile?.accept( object:JavaRecursiveElementVisitor() {
                    override fun visitIdentifier(identifier: PsiIdentifier) {
//                        super.visitIdentifier(identifier)
                        if(identifier.text == "PermissionDefiner") {
                            println("doDefinePermissions")
                           AcpUtils.collectAcpFromClass(identifier.parent.parent.parent as PsiClass)
                        }
                    }
//                    override fun visitClass(aClass: PsiClass) {
//                        println("visitFile")
//                        println(aClass)
//                        println(aClass.references)
//                        aClass.references.forEach {
//                            println(it.resolve())
//                        }
//                        if(aClass?.name == "Acp.kt") {
//                            println("Acp.kt changed")
//                            project.getService(AcpService::class.java).collectAcpDefinition()
//                        }
//                    }
                })
            }
        }
    }
}