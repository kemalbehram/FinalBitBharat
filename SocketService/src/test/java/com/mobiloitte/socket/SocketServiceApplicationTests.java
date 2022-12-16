package com.mobiloitte.socket;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SocketServiceApplicationTests {

	@Test
	public void contextLoads() {
		List<String> list = Arrays.asList("asd", "asaaa", "sdas", "ddd");
		list.stream().filter(i -> i.matches("a")).collect(Collectors.toList());
	}

}
