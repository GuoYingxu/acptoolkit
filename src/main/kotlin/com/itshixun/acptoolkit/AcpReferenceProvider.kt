package com.itshixun.acptoolkit

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.elementType
import com.intellij.psi.util.findParentOfType
import com.intellij.util.ProcessingContext


class AcpReferenceProvider :PsiReferenceProvider(){
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
//        println(element.elementType)
        if(element is PsiLiteralExpression) {
            var parentAnnotation = element.findParentOfType<PsiAnnotation>()
//            println(parentAnnotation?.nameReferenceElement?.text)
                if(parentAnnotation?.nameReferenceElement?.text == "Spica") {
                    val parentPsiNameValuePair = element.findParentOfType<PsiNameValuePair>()
                        if(parentPsiNameValuePair?.name == "expr" && element.value!=null) {

                            val text = element.text
                            if(text!=null) {
                                if(text.contains(',')) {
                                    return arrayOf(AcpReference(element, TextRange(text.lastIndexOf(',')+1, text.length - 1)))

                                }else {
                                    return arrayOf(AcpReference(element, TextRange(1, text.length - 1)))
                                }
                            }
//                            println("elemetnValue:::::$text")
//                            return arrayOf(AcpReference(element,TextRange(1,text.length-1)))

//                            var text = element.value as String
//                            println("elemetnValue:::::$text")
//                            return arrayOf(AcpReference(element,TextRange(0,text.length)))
//                            val parts = text.split(",")
//                            println("${parts[0]},size::: ${parts.size}")
//                            if(parts.size == 1) {
//                                println("range:::::0,${text.length}")
//                                if(text.contains("IntellijIdeaRulezzz")) {
//                                    val textRange = TextRange(0,text.lastIndexOf("IntellijIdeaRulezzz"))
//                                    println("text:${text.substring(0,text.lastIndexOf("IntellijIdeaRulezzz"))}")
//                                    return arrayOf(AcpReference(element,textRange))
//                                }else {
//
//
//                                    val textRange = TextRange(0, text.length)
//                                    println("text:${text.substring(0,text.length)}")
//                                    return arrayOf(AcpReference(element, textRange))
//                                }
//                            }else {
//
//                                if(parts.last().contains("IntellijIdeaRulezzz")) {
//                                    val textRange = TextRange(text.lastIndexOf(',')+1,text.lastIndexOf("IntellijIdeaRulezzz"))
//                                    println("text:${text.substring(text.lastIndexOf(',')+1,text.lastIndexOf("IntellijIdeaRulezzz"))}")
//                                    return arrayOf(AcpReference(element,textRange))
//                                }else {
//                                    val textRange = TextRange(text.lastIndexOf(',')+1,text.length)
//                                    println("text:${text.substring(text.lastIndexOf(',')+1,text.length)}")
//                                    return arrayOf(AcpReference(element,textRange))
//                                }
//                            }
                        }
                }
        }

        return PsiReference.EMPTY_ARRAY

    }
}