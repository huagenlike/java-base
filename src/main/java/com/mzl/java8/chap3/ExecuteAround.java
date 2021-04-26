package com.mzl.java8.chap3;

import java.io.*;
public class ExecuteAround {

	public static void main(String ...args) throws IOException{

        // method we want to refactor to make more flexible
        String result = processFileLimited();
        System.out.println(result);

        System.out.println("---");

		// 第4步：传递Lambda
		// 处理一行
		String oneLine = processFile((BufferedReader b) -> b.readLine());
		System.out.println(oneLine);

		// 处理两行
		String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine());
		System.out.println(twoLines);
	}

	// 注意你使用了Java 7中的带资源的try语句，它已经简化了代码，因为你不需要显式地关闭资源了
    public static String processFileLimited() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/mzl/java8/chap3/data.txt"))) {
            return br.readLine(); // 这就是做有用工作得那行代码
        }
    }

	// 第3步：执行一个行为
	public static String processFile(BufferedReaderProcessor p) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/mzl/java8/chap3/data.txt"))){
			return p.process(br);
		}
	}

	// 第2步：使用函数式接口来传递行为
	@FunctionalInterface
	public interface BufferedReaderProcessor{
		String process(BufferedReader b) throws IOException;
	}
}
