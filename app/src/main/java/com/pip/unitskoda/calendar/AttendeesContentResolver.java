package com.pip.unitskoda.calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.List;

public class AttendeesContentResolver {
    public static final String[] FIELDS = {
            CalendarContract.Attendees.ATTENDEE_NAME,
            CalendarContract.Attendees.ATTENDEE_EMAIL,
            CalendarContract.Attendees.ATTENDEE_STATUS
    };

    public static final Uri CALENDAR_URI = CalendarContract.Attendees.CONTENT_URI;

    ContentResolver contentResolver;

    public AttendeesContentResolver(Context ctx) {
        contentResolver = ctx.getContentResolver();
    }

    public List<Attendee> getAllAttendees() {
        return getAttendees(null, new ArrayList<String>());
    }
    public List<Attendee> getAttendees(Long eventId, List<String> userModels) {
        // Fetch a list of all attendees sync'd with the device and their display names
        Cursor cursor = CalendarContract.Attendees.query(contentResolver, eventId, FIELDS);

        List<Attendee> attendees = new ArrayList<Attendee>();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String email = cursor.getString(1);
                    String status = cursor.getString(2);
                    attendees.add(new Attendee(name, email, status, false));
                }
            }
        } catch (AssertionError ex) { /*TODO: log exception and bail*/ }

        return attendees;
    }
}
