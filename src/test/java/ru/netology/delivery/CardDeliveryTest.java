package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    public String generatedDate(int date, String pattern) {
        return LocalDate.now().plusDays(date).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldSuccefullyBookCard() {
        String planningDate = generatedDate(5, "dd.MM.YYYY");
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick().press(Keys.BACK_SPACE).setValue(planningDate);
        $("[name='name']").setValue("Рахманинов Иван");
        $("[name='phone']").setValue("+71234567890");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldSuccefullyBookCardNextDifficulty() {
        int planningDays = 487;
        String planningDate = generatedDate(planningDays, "d.MM.yyyy");

        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Мо");
        $$(".menu-item__control").findBy(exactText("Тамбов")).click();
        $(".icon_name_calendar").click();

        int difYears = LocalDate.now().plusDays(planningDays).getYear()-LocalDate.now().getYear();
        int difMonths = LocalDate.now().plusDays(planningDays).getMonthValue()-LocalDate.now().getMonthValue();

        for(int i=0; i<difYears; i++) {
            $("[data-step='12']").click();
        }
        for(int j=0; j<difMonths; j++) {
            $("[data-step='1']").click();
        }
        $$(".calendar__day").find(exactText(generatedDate(planningDays, "d"))).click();

        $("[name='name']").setValue("Сергеев Лазарь");
        $("[name='phone']").setValue("+71234567890");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + planningDate));
    }
}

