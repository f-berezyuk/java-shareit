package ru.practicum.shareit;

import java.time.LocalDateTime;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.EBookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@AutoConfigureJsonTesters
public class DtoJsonTest {

    private JacksonTester<BookingDto> bookingJsonTester;
    private JacksonTester<ItemDto> itemJsonTester;
    private JacksonTester<UserDto> userJsonTester;

    @BeforeEach
    public void setup() {
        // Инициализация JacksonTester для каждого тестируемого класса
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testDeserializeUserDto() throws Exception {
        String json = "{\n" +
                "    \"id\": 1,\n" +
                "    \"email\": \"john.doe@example.com\",\n" +
                "    \"name\": \"John Doe\"\n" +
                "}";


        ObjectContent<UserDto> objectContent = userJsonTester.parse(json);
        UserDto userDto = objectContent.getObject();
        assertThat(userDto.getId()).isEqualTo(1L);
    }

    @Test
    public void testSerializeUserDto() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("john.doe@example.com")
                .name("John Doe")
                .build();

        JsonContent<UserDto> jsonContent = userJsonTester.write(userDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@example.com");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
    }

    @Test
    public void testDeserializeBookingDto() throws Exception {
        String json = "{\n" +
                      "    \"id\": 1,\n" +
                      "    \"status\": \"APPROVED\",\n" +
                      "    \"booker\": {\n" +
                      "        \"id\": 1,\n" +
                      "        \"email\": \"john.doe@example.com\",\n" +
                      "        \"name\": \"John Doe\"\n" +
                      "    }\n" +
                      "}\n";

        ObjectContent<BookingDto> objectContent = bookingJsonTester.parse(json);
        BookingDto bookingDto = objectContent.getObject();
        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStatus()).isEqualTo(EBookingStatus.APPROVED);
        assertThat(bookingDto.getBooker().getId()).isEqualTo(1L);
        assertThat(bookingDto.getBooker().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(bookingDto.getBooker().getName()).isEqualTo("John Doe");
    }


    @Test
    public void testSerializeBookingDto() throws Exception {
        UserDto booker = UserDto.builder()
                .id(1L)
                .email("john.doe@example.com")
                .name("John Doe")
                .build();

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 10, 1, 12, 0))
                .end(LocalDateTime.of(2023, 10, 1, 13, 0))
                .status(EBookingStatus.APPROVED)
                .booker(booker)
                .build();

        JsonContent<BookingDto> jsonContent = bookingJsonTester.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-01T12:00:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2023-10-01T13:00:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.email").isEqualTo("john.doe@example.com");
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo("John Doe");
    }

    @Test
    public void testSerializeItemDto() throws Exception {
        BookingDto lastBooking = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2023, 9, 30, 14, 0))


                .end(LocalDateTime.of(2023, 9, 30, 15, 0))
                .status(EBookingStatus.WAITING)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("A test item")
                .available(true)
                .comments(Collections.emptyList())
                .lastBooking(lastBooking)
                .nextBooking(null)
                .requestId(10L)
                .build();

        JsonContent<ItemDto> itemJsonContent = itemJsonTester.write(itemDto);

        assertThat(itemJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(itemJsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Test Item");
        assertThat(itemJsonContent).extractingJsonPathStringValue("$.description").isEqualTo("A test item");
        assertThat(itemJsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(itemJsonContent).extractingJsonPathArrayValue("$.comments").isEmpty();
        assertThat(itemJsonContent).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(2);
        assertThat(itemJsonContent).extractingJsonPathStringValue("$.lastBooking.status").isEqualTo("WAITING");
        assertThat(itemJsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(10);
    }
}
