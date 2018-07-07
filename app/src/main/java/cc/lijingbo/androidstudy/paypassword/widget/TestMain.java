package cc.lijingbo.androidstudy.paypassword.widget;

/**
 * @作者: lijingbo
 * @日期: 2018-06-04 16:53
 */

public class TestMain<K, V> {

    public <P> TestMain(P p) {
        System.out.println(p);
    }

    public <K, V> void main(K k, V v) {
        System.out.println(k);
        System.out.println(v);
    }

    public <T> String fun1(T t) {
        return "xxx";
    }

    public <T> T fun2(T t) {
        return t;
    }

}
