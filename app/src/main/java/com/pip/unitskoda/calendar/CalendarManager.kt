package com.pip.unitskoda.calendar

import android.provider.CalendarContract.Calendars
import android.content.Context
import android.util.Log
import it.macisamuele.calendarprovider.CalendarInfo
import it.macisamuele.calendarprovider.EventInfo
import java.util.*


object CalendarManager {

    @JvmStatic
    fun getCalendars(context: Context): List<CalendarInfo> {
        return CalendarInfo.getAllCalendars(context)
    }

    @JvmStatic
    fun getCalendarStrings(calendars: List<CalendarInfo>): List<String> {
        return calendars.map { it.displayName }
    }

    @JvmStatic
    fun getCurrentEventsOfCalendar(context: Context, calendar: CalendarInfo): List<EventInfo> {
        return EventInfo.getEvents(context, Date(), Date(), listOf(calendar.id.toInt()), null)
    }

    @JvmStatic
    fun getAttendeesOfEvent(context: Context, eventInfo: EventInfo): List<Attendee> {
        return AttendeesContentResolver(context).getAttendees(eventInfo.id)
    }
}