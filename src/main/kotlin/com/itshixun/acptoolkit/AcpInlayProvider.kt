package com.itshixun.acptoolkit

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.service
import com.intellij.openapi.components.services
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiAnnotationParameterList
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.PsiNameValuePair
import com.intellij.psi.PsiParameterList

class AcpInlayProvider:InlayHintsProvider {
    companion object {
        const val PROVIDER_ID: String = "java.acp.hints"
    }
    override fun createCollector(file: PsiFile, editor: Editor): InlayHintsCollector? {
        if(file.project.getService(AcpService::class.java).initialized()) {
            return Collector()
        }else {
            file.project.getService(AcpService::class.java).collectAcpDefinition();
            return Collector()
        }

    }
    private class Collector: SharedBypassCollector{
        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if( element is PsiAnnotation) {
                AcpUtils.findSpicaRights(element)?.let {
                    if(it.value != null) {
                        val name = it.value as String
                        val description = AcpUtils.findRightsDescription(name)
                        description?.let {
                            sink.addPresentation(InlineInlayPosition(element.textRange.endOffset, true), hasBackground = true) {
                                text(it)
                            }
                        }
                    }
                }
            }
        }


    }
}