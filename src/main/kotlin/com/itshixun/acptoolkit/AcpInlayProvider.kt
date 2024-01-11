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
        //TODO("Not yet implemented")
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
//                println("collectFromElement:${element.nameReferenceElement?.text}")
//                if(element.nameReferenceElement?.text == "Spica") {
//                    element.children.forEach { child ->
//                        if(child is PsiAnnotationParameterList) {
//                            child.attributes.forEach { param ->
//                                if(param is PsiNameValuePair) {
//                                    if(param.name == "expr") {
//                                        val rights = param.value
//                                        println(rights)
//                                    }
//                                }
//                            }
//                        }
//                            logger<AcpInlayProvider>().info("child:${child.text}")
//                            if(child.name == "name") {
//                                child.value?.let {
//                                    if(it is PsiLiteralValue) {
//
//                                        (it as PsiLiteralValue).textRange?.let {
//                                            logger<AcpInlayProvider>().info("it:${it.startOffset},${it.endOffset}")
//                                        }
//                                        val startOffset = (it as PsiLiteralValue).textRange?.startOffset
//                                        val endOffset = (it as PsiLiteralValue).textRange?.endOffset
//                                        if(endOffset == null || startOffset == null) {
//                                            return
//                                        }
//                                        sink.addPresentation(InlineInlayPosition(endOffset,true), hasBackground = true){
//                                            text("Spica")
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }


    }
}