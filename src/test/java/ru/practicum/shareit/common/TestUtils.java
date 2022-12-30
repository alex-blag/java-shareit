package ru.practicum.shareit.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingNearest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentPostDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static final String X_SHARER_USER_ID = CommonUtils.X_SHARER_USER_ID;

    public static final long USER1_ID = 1L;

    public static final long ITEM1_ID = 10L;
    public static final long ITEM1_OWNER_ID = 12L;
    public static final long ITEM1_REQUEST_ID = 14L;

    public static final long COMMENT1_ID = 20L;
    public static final long COMMENT1_AUTHOR_ID = 22L;

    public static final LocalDateTime BOOKING1_START = LocalDateTime.now().plusMinutes(1L);
    public static final LocalDateTime BOOKING1_END = LocalDateTime.now().plusMinutes(2L);

    public static final long BOOKING1_ID = 30L;
    public static final long BOOKING1_BOOKER_ID = 32L;
    public static final long LAST_BOOKING_ID = 40L;
    public static final long LAST_BOOKING_BOOKER_ID = 42L;
    public static final long NEXT_BOOKING_ID = 50L;
    public static final long NEXT_BOOKING_BOOKER_ID = 52;

    public static User getUser1() {
        return new User(USER1_ID, "user1Name", "user1@mail.com");
    }

    public static UserDto getUser1WithoutIdDto() {
        return new UserDto(null, "user1Name", "user1@mail.com");
    }

    public static UserDto getUser1NameUpdateDto() {
        return new UserDto(null, "user1NameUpdate", null);
    }

    public static User getUser1NameUpdate() {
        return new User(USER1_ID, "user1NameUpdate", null);
    }

    public static User getUser1WithUpdatedName() {
        return new User(USER1_ID, "user1NameUpdate", "user1@mail.com");
    }

    public static User getUser1EmailUpdate() {
        return new User(USER1_ID, null, "user1Update@mail.com");
    }

    public static ItemPostDto getItem1PostDto() {
        return new ItemPostDto("item1Name", "item1Description", true, null);
    }

    public static Item getItem1() {
        return new Item(
                ITEM1_ID,
                "item1Name",
                "item1Description",
                true,
                ITEM1_OWNER_ID,
                ITEM1_REQUEST_ID,
                null,
                null,
                List.of(getItem1Comment())
        );
    }

    public static ItemPostDto getItem1NameUpdatePostDto() {
        return new ItemPostDto("item1Update", null, null, null);
    }

    public static CommentPostDto getComment1PostDto() {
        return new CommentPostDto("comment1Text");
    }

    public static Comment getItem1Comment() {
        return new Comment(COMMENT1_ID, "comment1Text", getComment1Item(), getComment1Author(), LocalDateTime.now());
    }

    public static Item getComment1Item() {
        return new Item(
                ITEM1_ID,
                "item1Name",
                "item1Description",
                true,
                ITEM1_OWNER_ID,
                null,
                null,
                null,
                null
        );
    }

    public static User getComment1Author() {
        return new User(COMMENT1_AUTHOR_ID, "comment1Author", "comment1Author@mail.com");
    }

    public static BookingPostDto getBooking1PostDto() {
        return new BookingPostDto(
                BOOKING1_START,
                BOOKING1_END,
                ITEM1_ID
        );
    }

    public static Booking getBooking1() {
        return new Booking(
                BOOKING1_ID,
                BOOKING1_START,
                BOOKING1_END,
                getItem1(),
                getBooking1BookerId(),
                Status.WAITING
        );
    }

    public static User getBooking1BookerId() {
        return new User(BOOKING1_BOOKER_ID, "booking1Booker", "booking1Booker@mail.com");
    }

    public static Booking getBooking1Approved() {
        return new Booking(
                BOOKING1_ID,
                BOOKING1_START,
                BOOKING1_END,
                getItem1(),
                getUser1(),
                Status.APPROVED
        );
    }

    public static Booking getItem1LastBooking() {
        return new Booking(
                LAST_BOOKING_ID,
                BOOKING1_START,
                BOOKING1_END,
                getItem1(),
                getLastBookingBooker(),
                Status.APPROVED
        );
    }

    private static User getLastBookingBooker() {
        return new User(LAST_BOOKING_BOOKER_ID, "lastBookingBooker", "lastBookingBooker@mail.com");
    }

    public static Booking getItem1NextBooking() {
        return new Booking(
                NEXT_BOOKING_ID,
                BOOKING1_START,
                BOOKING1_END,
                getItem1(),
                getNextBookingBooker(),
                Status.APPROVED
        );
    }

    private static User getNextBookingBooker() {
        return new User(NEXT_BOOKING_BOOKER_ID, "nextBookingBooker", "nextBookingBooker@mail.com");
    }

    public static Item getItem1NameUpdate() {
        return new Item(
                ITEM1_ID,
                "item1NameUpdate",
                null,
                null,
                ITEM1_OWNER_ID,
                null,
                null,
                null,
                null
        );
    }

    public static List<Item> getItemsByOwnerId() {
        return List.of(getItemByOwnerId());
    }

    private static Item getItemByOwnerId() {
        return new Item(
                ITEM1_ID,
                "item1Name",
                "item1Description",
                true,
                ITEM1_OWNER_ID,
                ITEM1_REQUEST_ID,
                getItem1LastBookingNearest(),
                getItem1NextBookingNearest(),
                getItem1Comments()
        );
    }

    private static BookingNearest getItem1LastBookingNearest() {
        return new BookingNearest(
                LAST_BOOKING_ID,
                BOOKING1_START,
                BOOKING1_END,
                BOOKING1_BOOKER_ID);
    }

    private static BookingNearest getItem1NextBookingNearest() {
        return new BookingNearest(
                NEXT_BOOKING_ID,
                BOOKING1_START,
                BOOKING1_END,
                BOOKING1_BOOKER_ID
        );
    }

    private static List<Comment> getItem1Comments() {
        return List.of(getItem1Comment());
    }

    public static Booking getBooking1StatusUpdate() {
        return new Booking(
                BOOKING1_ID,
                BOOKING1_START,
                BOOKING1_END,
                getItem1(),
                getBooking1BookerId(),
                Status.APPROVED
        );
    }

    public static User getUserWithoutId() {
        return new User(null, "userName", "user@mail.com");
    }

    public static Item getItemWithoutId() {
        return new Item(
                null,
                "item_name",
                "item_description",
                true,
                null,
                null,
                null,
                null,
                null
        );
    }

}