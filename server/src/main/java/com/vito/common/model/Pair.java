/**
 * 
 * 类描述：由两个元素组成的对象
 *
 */

package com.vito.common.model;

import java.io.Serializable;

/**
 * <pre><b><font color="blue">Pair</font></b></pre>
 *
 * <pre><b>&nbsp;--描述说明--</b></pre>
 * <pre>由两个元素组成的对象</pre>
 * <pre>
 * <b>--样例--</b>
 *   Pair obj = new Pair();
 * </pre>
 * JDK版本：JDK1.4
 * @author  <b>zhaixm</b>
 */
public class Pair<F, S> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private F first;
    private S second;
   
    public Pair() {
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

	/**
     * @return the first
     */
    public F getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public S getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(S second) {
        this.second = second;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(first != null) {
			sb.append("first:" + first.toString());
		}
		if(second != null) {
			sb.append(" second:" + second.toString());
		}
		return sb.toString();
	}
    
}
