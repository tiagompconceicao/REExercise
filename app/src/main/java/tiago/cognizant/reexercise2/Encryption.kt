package tiago.cognizant.reexercise2

class Encryption {
    fun decrypt(stringToEncrypt: String): String {
        val secret = "mysecret"
        val s = StringBuilder()
        for (i in stringToEncrypt.indices) {
            val a1 = stringToEncrypt[i].code
            val a2 = secret[i % secret.length].code
            val res = a1 xor a2
            s.append(res.toChar())
        }
        return s.toString()
    }
}

