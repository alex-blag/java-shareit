package ru.practicum.shareit.server.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingNearest;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.dto.UserMapper;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        ItemDto itemDto = ItemMapper.toItemDto(booking.getItem());
        bookingDto.setItem(itemDto);

        UserDto userDto = UserMapper.toUserDto(booking.getBooker());
        bookingDto.setBooker(userDto);

        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking toBooking(BookingPostDto bookingPostDto) {
        Booking booking = new Booking();
        booking.setStart(bookingPostDto.getStart());
        booking.setEnd(bookingPostDto.getEnd());

        Item item = new Item();
        item.setId(bookingPostDto.getItemId());
        booking.setItem(item);

        User user = new User();
        booking.setBooker(user);
        return booking;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        ItemDto itemDto = bookingDto.getItem() != null
                ? bookingDto.getItem()
                : new ItemDto();
        Item item = ItemMapper.toItem(itemDto);
        booking.setItem(item);

        UserDto userDto = bookingDto.getBooker() != null
                ? bookingDto.getBooker()
                : new UserDto();
        User booker = UserMapper.toUser(userDto);
        booking.setBooker(booker);

        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static List<BookingDto> toBookingsDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors
                        .toList());
    }

    public static BookingNearestDto toBookingNearestDto(BookingNearest bookingNearest) {
        BookingNearestDto bookingNearestDto = new BookingNearestDto();
        bookingNearestDto.setId(bookingNearest.getId());
        bookingNearestDto.setBookerId(bookingNearest.getBookerId());
        bookingNearestDto.setStart(bookingNearest.getStart());
        bookingNearestDto.setEnd(bookingNearest.getEnd());
        return bookingNearestDto;
    }

}
