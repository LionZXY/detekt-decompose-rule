package uk.kulikov.detekt.decompose

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test
import uk.kulikov.detekt.decompose.rules.PushForbiddenRule

@KotlinCoreEnvironmentTest
internal class PushForbiddenRuleTest(private val env: KotlinCoreEnvironment) {

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
        val findings = PushForbiddenRule(Config.empty).compileAndLintWithContext(env, code)
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
        val findings = PushForbiddenRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
