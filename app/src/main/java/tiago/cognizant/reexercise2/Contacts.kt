package tiago.cognizant.reexercise2

class Contacts {
    fun getNames(string: String): String {
        val str = "contacts"
        val s = StringBuilder()
        for (i in string.indices) {
            val a1 = string[i].code
            val a2 = str[i % str.length].code
            val res = a1 xor a2
            s.append(res.toChar())
        }
        return s.toString()
    }
}

