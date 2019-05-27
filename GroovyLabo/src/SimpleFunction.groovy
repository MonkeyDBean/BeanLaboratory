/**
 * Created by MonkeyBean on 2019/5/24.
 */
class SimpleFunction {

    /**
     * 年龄分段
     */
    static rangeAge(age) {
        if (age < 0 || age > 150) {
            return "超人类年龄"
        }
        switch (age) {
            case 0..17:
                return '未成年'
            case 18..30:
                return '青年'
            case 31..50:
                return '中年'
            default:
                return '老年'
        }
    }
}
