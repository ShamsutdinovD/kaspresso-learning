package lesson07

import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import java.awt.Color

enum class Color { RED, BLUE, GREEN, YELLOW, BLACK, WHITE }
val shapes = listOf(
    Shape(10f, 3, Color.RED), Shape(5f, 4, Color.BLUE), Shape(7f, 2, Color.GREEN),
    Shape(0.5f, 1, Color.YELLOW), Shape(-3f, 5, Color.BLACK), Shape(8f, -2, Color.WHITE),
    Shape(12f, 6, Color.RED), Shape(15f, 8, Color.BLUE), Shape(20f, 4, Color.GREEN),
    Shape(9f, 5, Color.YELLOW), Shape(2f, 3, Color.BLACK), Shape(11f, 7, Color.WHITE),
    Shape(6f, 10, Color.RED), Shape(3f, 2, Color.BLUE), Shape(4f, 1, Color.GREEN),
    Shape(25f, 12, Color.YELLOW), Shape(30f, 14, Color.BLACK), Shape(35f, 16, Color.WHITE),
    Shape(40f, 18, Color.RED), Shape(50f, 20, Color.BLUE)
)

data class Shape(val sideLength: Float, val sides: Int, val color: Color)

// Матчер для проверки длины стороны в заданном диапазоне
class SideLengthMatcher(private val min: Float, private val max: Float) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("side length from $min to $max")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.sideLength in min..max).also {
            if (!it) {
                mismatchDescription.appendText("side length was ")
                    .appendValue(item.sideLength)
            }
        }
    }
}

// Матчер для проверки отрицательной длины стороны
class NegativeSideLengthMatcher : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("side length is positive")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.sideLength >= 0).also {
            if (!it) mismatchDescription.appendText("side length was negative")
        }
    }
}


// Матчер для проверки отрицательного количества сторон
class NegativeSidesMatcher : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("side number is positive")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.sides >= 0).also {
            if (!it) {
                mismatchDescription.appendText("side number was negative")
            }
        }
    }
}

// Матчер для проверки количества углов
class CornerNumberMatcher(private val cornerNumber: Int) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("corner number")
            .appendValue(cornerNumber)
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        val actualCorner = if (item.sides <= 2) 0 else item.sides
        return (cornerNumber == actualCorner).also {
            if (!it) {
                mismatchDescription.appendText("corner number was ")
                    .appendValue(actualCorner)
            }
        }
    }
}

class EvenSides : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("side number is even")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.sides % 2 == 0).also {
            if (!it) {
                mismatchDescription.appendText("side number wasn't even")
            }
        }
    }
}

// Матчер для проверки цвета
class ColorMatcher(private val expectedColor: Color) : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("фигура должна иметь цвет $expectedColor")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.color == expectedColor).also {
            if (!it) {
                mismatchDescription.appendText("color was ")
                    .appendValue(item.color)
            }
        }
    }
}

fun hasCorners(cornerNumber: Int) = CornerNumberMatcher(cornerNumber)
fun hasValidSides() = NegativeSidesMatcher()
fun hasValidSideLength() = NegativeSideLengthMatcher()
fun hasSideLengthInRange(min: Float, max: Float) = SideLengthMatcher(min, max)
fun hasEvenSides() = EvenSides()
fun hasColor(expectedColor: Color) = ColorMatcher(expectedColor)

// Фильтрация с использованием allOf
val filteredShapes = shapes.filter { shape ->
    allOf(
        hasSideLengthInRange(1f, 20f),
        hasEvenSides(),
        hasValidSideLength(),
        hasValidSides()
    ).matches(shape)
}

// Примеры ассертов
fun testMatchers() {
    val shape = Shape(10f, 4, Color.RED)
    assertThat(shape, hasSideLengthInRange(1f, 20f))
    assertThat(shape, hasColor(Color.RED))
    assertThat(shape, hasValidSideLength())
    assertThat(shape, hasValidSides())
}

fun main() {
    testMatchers()
    println("Фигуры, прошедшие фильтрацию: $filteredShapes")
}