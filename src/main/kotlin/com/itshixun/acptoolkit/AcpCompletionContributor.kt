package com.itshixun.acptoolkit

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.emmet.tokens.StringLiteralToken
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns
import com.intellij.patterns.StringPattern
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.JavaElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull
import java.util.*


private val itemSet = hashSetOf<String>()

class AcpInserthandler : InsertHandler<LookupElement> {
    override fun handleInsert(p0: InsertionContext, p1: LookupElement) {
        // 插入到光标位置
        p0.document.insertString(p0.tailOffset, " - Option 1")
    }

}

class AcpCompletionContributor: CompletionContributor() {
        val log: Logger = Logger.getInstance(AcpCompletionContributor::class.java)

    init {
//        extend(CompletionType.BASIC,
//                psiElement().inside(psiElement(PsiFile::class.java))
//                ,StringLiteralCompletionProvider())
//
//        extend(CompletionType.BASIC,
//                PlatformPatterns.psiElement(JavaTokenType.STRING_LITERAL).withParent(psiElement(PsiJavaCodeReferenceElement::class.java).withText("@Spica")),
//                        .withElementType(JavaElementType.LITERAL_EXPRESSION),
//psiElement().with(
//      object:  PatternCondition<PsiElement>("Spica") {
//          override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
//              println("accepts$t")
//              if (t is PsiLiteralExpression) {
//                  val literalValue = t.text
//                  println("literalValue: $literalValue")
//                  if (literalValue =="@Spica") {
//                      return true
//                  }
//              }
//              return false
//          }
//
//        },
//),
//                        object : CompletionProvider<CompletionParameters?>() {
//                    override fun addCompletions( parameters: CompletionParameters,
//                                                context: ProcessingContext,
//                                                 resultSet: CompletionResultSet) {
//                        println("addCompletions")
//                        // 这里添加自定义补全建议的逻辑
//                        resultSet.addElement(LookupElementBuilder.create("value1"))
//                        resultSet.addElement(LookupElementBuilder.create("value2"))
//                        // 添加更多的补全建议...
//                    }
//
//                }
//        )


        // 匹配字符串
//        extend(CompletionType.BASIC,
//                psiElement(PsiPlainText::class.java)
//               ,
//                object: CompletionProvider<CompletionParameters>() {
//                    override fun addCompletions(parameters: CompletionParameters,
//                                                context: ProcessingContext,
//                                                result: CompletionResultSet) {
//                        // 获取当前光标位置的 PsiElement
//                        val position = parameters.position
//                        println(position)
//                        // 检查当前位置是否为字符串字面量
//                        if (position is PsiLiteralExpression) {
//                            // 在这里实现你的字符串字面量补全逻辑
//                            val literalValue = position.text
//                            println("literalValue: $literalValue")
//                            // 添加一些简单的补全项，实际中你可能需要根据具体需求来生成补全项
//                            result.addElement(LookupElementBuilder.create("value1"))
//                            result.addElement(LookupElementBuilder.create("value2"))
//
//                            // 添加更多补全项...
//
//                            // 设置前缀匹配器，确保只有以当前字符串开头的建议项才会显示
////                            result = result.withPrefixMatcher(PrefixMatcher.create(literalValue))
//                        }
//                    }
//
//                })

        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(PsiLiteralExpression::class.java)
                        .withText(PlatformPatterns.string().startsWith("\"")),

                object: CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            resultSet: CompletionResultSet
                    ) {
                        val position = parameters.position
                        println(position)
                        // 判断是否满足条件，只有在带有特定注解的参数上触发自动补全
                        if (isAutoCompleteStringParameter(position)) {
                            val options = getAutoCompleteOptions()
                            resultSet.addAllElements(options.map { LookupElementBuilder.create(it) })
                        }
                    }
                })
    }

    private fun isAutoCompleteStringParameter(element: PsiElement): Boolean {
        val parameter = findParameterAnnotatedWithAutoCompleteString(element)
        return parameter != null
    }

    private fun findParameterAnnotatedWithAutoCompleteString(element: PsiElement): PsiParameter? {
        val method = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        return method?.parameterList?.parameters?.find {
            it.annotations.any { annotation ->
                println("scsa")
                annotation.qualifiedName == "Spicas" // 替换为实际的注解包名
            }
        }
    }

    private fun getAutoCompleteOptions(): List<String> {
        // 获取参数的候选项列表
        // 可以从数据库、文件或其他数据源中读取
        return listOf("option1", "option2", "option3")
    }
    private class StringLiteralCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters,
                                    context: ProcessingContext,
                                    result: CompletionResultSet) {
            // 获取当前光标位置的 PsiElement

            var result = result
            val position =  parameters.position
            println(position)
            // 检查当前位置是否为字符串字面量
//            if (position is PsiLiteralExpression) {
                // 在这里实现你的字符串字面量补全逻辑
                val literalValue = position.text
                println("literalValue: $literalValue")
                // 添加一些简单的补全项，实际中你可能需要根据具体需求来生成补全项
                result.addElement(LookupElementBuilder.create("HELLOE").withInsertHandler(AcpInserthandler()))
                result.addElement(LookupElementBuilder.create("Hero- Option 2").withInsertHandler(AcpInserthandler()))

                // 添加更多补全项...

                // 设置前缀匹配器，确保只有以当前字符串开头的建议项才会显示
//                result = result.withPrefixMatcher(PrefixMatcher.create(literalValue))
//            }
        }

    }
}




