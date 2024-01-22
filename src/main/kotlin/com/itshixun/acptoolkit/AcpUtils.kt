package com.itshixun.acptoolkit

import com.intellij.psi.*
import com.intellij.psi.util.PsiUtil
import com.intellij.psi.util.findParentOfType
import org.jetbrains.annotations.NotNull

class AcpUtils {
    fun test() {
        println("test")
    }
    companion object {

        fun clearAcpDefinitionMap() {
            AcpCacheData.getInstance().clear()
        }
        fun getAcpDict():List<AcpDefinition> {
            return AcpCacheData.getInstance().acpDefinitionMap.values.toList()
        }
        /**
         * 匹配spica 权限参数
         */
        fun findSpicaRights(annotation: PsiAnnotation): PsiLiteralExpression? {
            if (annotation.nameReferenceElement?.text == "Spica") {
                annotation.children.forEach { child ->
                    if (child is PsiAnnotationParameterList) {
                        child.attributes.forEach { param ->
                            if (param is PsiNameValuePair) {
                                if (param.name == "expr") {
                                    val rights = param.value
                                    if (rights is PsiLiteralExpression) {
                                        return rights
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null
        }

        fun isAcpPsiLiteralExpression(element:PsiElement):Boolean {
            if(element is PsiLiteralExpression) {
                var parentAnnotation = element.findParentOfType<PsiAnnotation>()
                if(parentAnnotation != null) {
                    if(parentAnnotation.nameReferenceElement?.text == "Spica") {
                        val parentPsiNameValuePair = element.findParentOfType<PsiNameValuePair>()
                        if(parentPsiNameValuePair != null) {
                            if(parentPsiNameValuePair.name == "expr") {
                                return true
                            }
                        }
                    }
                }
            }
            return false

        }

        fun findRightsDescription(nameValue:String):String? {
            val name = nameValue.trim()
//        println(name)
            if(name.isBlank()) return null
            if(name.contains(',')) {
                val names = name.split(',')
                val descriptions = names.map { findRightsDescription(it) }
                return descriptions.filterNotNull().joinToString(",")
            }else {
                return AcpCacheData.getInstance().acpDefinitionMap[name]?.description ?: null
            }
        }
        fun collectAcpFromClass(psiClass:PsiClass ) {
            psiClass.methods.forEach { method ->
                method.accept(object : JavaRecursiveElementVisitor() {

                    override fun visitMethodCallExpression(@NotNull expression: PsiMethodCallExpression) {
                        super.visitMethodCallExpression(expression)
                        if (expression.methodExpression.textMatches("definePermission")) {

                            val arguments: PsiExpressionList = expression.argumentList
                            if (arguments.expressions.size >= 4) {
                                val module = resolveText( arguments.expressions[0]);
                                val name = resolveText( arguments.expressions[1]);
                                val description = resolveText( arguments.expressions[3]);
                                val referenecExpression  = resolveExpression(arguments.expressions[1])
                                println("module:$module,name:$name,description:$description")
                                AcpCacheData.getInstance().acpDefinitionMap[name] = AcpDefinition(module,description,name,referenecExpression)
                            }

                        }
                    }
                })
            }


        }
        fun resolveExpression(expression:PsiExpression):PsiExpression {
            if(expression is PsiLiteralExpression) {
                return expression
            }
            if(expression is PsiReferenceExpression) {
                val resolve = expression.resolve()
                if(resolve is PsiField) {
                    return resolveExpression(resolve.initializer!!)
                }
            }
            return expression
        }
        fun resolveText(expression:PsiExpression):String {
            if(expression is PsiLiteralExpression) {
                return expression.value.toString()
            }
            if(expression is PsiReferenceExpression) {
                val resolve = expression.resolve()
                if(resolve is PsiField) {
                    return resolveText(resolve.initializer!!)
                }
            }
            return ""
        }

    }

}