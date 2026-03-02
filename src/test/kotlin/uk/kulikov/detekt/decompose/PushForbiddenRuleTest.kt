package uk.kulikov.detekt.decompose

import dev.detekt.api.Config
import dev.detekt.test.junit.KotlinCoreEnvironmentTest
import dev.detekt.test.lintWithContext
import dev.detekt.test.utils.KotlinEnvironmentContainer
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import uk.kulikov.detekt.decompose.rules.PushForbiddenRule

@KotlinCoreEnvironmentTest
internal class PushForbiddenRuleTest(private val env: KotlinEnvironmentContainer) {

    private val decomposeStub = """
        package com.arkivanov.decompose.router.stack

        class StackNavigation<T>

        fun <T : Any> StackNavigation<T>.push(configuration: T) {}
        fun <T : Any> StackNavigation<T>.pushToFront(configuration: T) {}
    """

    @Test
    fun `reports push decompose use`() {
        val code = """
        import com.arkivanov.decompose.router.stack.push
        import com.arkivanov.decompose.router.stack.StackNavigation

        fun test() {
            val navigation = StackNavigation<String>()
            navigation.push("Test")
        }
        """
        val findings = PushForbiddenRule(Config.empty).lintWithContext(env, code, decomposeStub)
        findings shouldHaveSize 2
    }

    @Test
    fun `doesn't reports pushToFront decompose use`() {
        val code = """
        import com.arkivanov.decompose.router.stack.StackNavigation
        import com.arkivanov.decompose.router.stack.pushToFront

        fun test() {
            val navigation = StackNavigation<String>()
            navigation.pushToFront("Test")
        }
        """
        val findings = PushForbiddenRule(Config.empty).lintWithContext(env, code, decomposeStub)
        findings shouldHaveSize 0
    }
}
