package lesson04

import kotlin.random.Random

// 1. Доступ к элементу по индексу ([ ]) и проверка наличия через in
//Есть класс Inventory, внутри которого хранится список строк items.
//Перегрузи оператор + чтобы добавлять новые элементы в список.
//Перегрузи оператор [ ], чтобы получать предмет по индексу.
//Перегрузи оператор in, чтобы проверять вхождение строки в список items.

class Inventory() {

    private val items = mutableListOf<String>()

    operator fun plus(item: String) {
        items.add(item)
    }

    operator fun get(index: Int): String {
        return items[index]
    }

    operator fun contains(item: String): Boolean {
        return item in items
    }
}

// 2. Инверсия состояния (!)
//Есть класс Toggle с полем enabled: Boolean.
//Перегрузи оператор !, чтобы он возвращал новый объект с противоположным состоянием.

class Toggle(private val enabled: Boolean) {

    operator fun not(): Toggle {
        return Toggle(!enabled)
    }

    override fun toString(): String {
        return enabled.toString()
    }
}

// 3. Умножение значения (*)
//Есть класс Price с полем amount: Int.
//Перегрузи оператор *, чтобы можно было умножать цену на целое число (например, количество товаров)

class Price(private val amount: Int) {

    operator fun times(number: Int): Int {
        return amount * number
    }
}

// 4. Диапазон значений (..)
//Есть класс Step с полем number: Int.
//Перегрузи оператор .., чтобы можно было создавать диапазон шагов между двумя объектами Step.
//Сделай возможной проверку: входит ли один Step в диапазон шагов с помощью оператора in. Обрати внимание,
// что это обратная операция и нужно расширять класс IntRange для проверки вхождения в него Step.

class Step(val number: Int) {

    operator fun rangeTo(other: Step): IntRange {
        return number..other.number
    }
}

operator fun IntRange.contains(step: Step): Boolean {
    return step.number in this
}

// 5. Последовательное объединение (+)
//Есть класс Log с полем entries: List<String>.
//Перегрузи оператор +, чтобы при сложении логов записи объединялись в один лог.

class Log() {

    private val entries = mutableListOf<String>()

    operator fun plus(entry: String): Log {
        entries.add(entry)
        return this
    }

    fun print() {
        println(entries.joinToString())
    }
}

fun main() {
    // 1
    val inventory = Inventory()
    inventory + "1"
    inventory + "2"
    println(inventory[1])
    println("1" in inventory)

    //2
    val toggle = Toggle(false)
    println(!toggle)

    //3
    val price = Price(34)
    println(price * 5)

    //4
    val stepFrom = Step(4)
    val stepTo = Step(10)
    val stepBetween = Step(6)
    val range = stepFrom..stepTo
    println(range.joinToString())
    println(stepBetween in range)

    //5
    val log = Log()
    log + "1" + "2" + "3" + "4"
    log.print()
}

// 6. Генератор фраз.
//Используй класс Person из "общих рекомендаций" ниже. Добавь в этот класс три инфиксные функции:
//
//says должна принимать строку, добавлять её в список фраз и возвращать этот же объект Person для
// дальнейшей работы. Всегда вызывается первой.
//and работает так же как и says, но не может быть вызвана первой (в этом случае нужно выкидывать
// IllegalStateException).
//or должна принимать строку и заменять последнюю фразу в списке фраз, выбирая случайным образом
// переданную строку или последнюю фразу из списка фраз с помощью метода selectPhrase.
// Так же должна возвращать текущий объект Person для дальнейшей работы. Так же не может быть вызвана первой,
// иначе выбрасывает IllegalStateException.

class Person(private val name: String) {

    private val phrases = mutableListOf<String>()

    infix fun says(phrase: String): Person {
        phrases.add(phrase)
        return this
    }

    infix fun and(phrase: String): Person {
        check(phrases.isNotEmpty()) { "Сначала используй says" }
        phrases.add(phrase)
        return this
    }

    infix fun or(phrase: String): Person {
        check(phrases.isNotEmpty()) { "Сначала используй says" }
        phrases[phrases.lastIndex] = selectPhrase(phrases[phrases.lastIndex], phrase)
        return this
    }

    fun print() {
        println(phrases.joinToString(" "))
    }

    private fun selectPhrase(first: String, second: String): String {
        val random = Random.nextInt(0, 2)
        return if (random == 0) first else second
    }
}

fun main2() {
    val andrew = Person("Andrew")
    andrew says "Hello" and "brothers." or "sisters." and "I believe" and "you" and "can do it" or "can't"
    andrew.print()
}