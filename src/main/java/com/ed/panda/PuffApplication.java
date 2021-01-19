package com.ed.panda;

import com.ed.panda.puff.anno.EnablePuff;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : Edward
 * @date : 2020/11/2 下午3:37
 */
@SpringBootApplication
@EnablePuff(basePackages = "com.ed.panda")
public class PuffApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuffApplication.class);
    }
}
