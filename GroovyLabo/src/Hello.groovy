/**
 * Created by MonkeyBean on 2019/5/24.
 */
import SimpleFunction

class Hello {
    static void main(args) {
        println 'hello world'
        def numbers = [1, 2, 3]
        assert numbers instanceof List
        println SimpleFunction.rangeAge(10)
    }
}