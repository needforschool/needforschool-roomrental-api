package fr.antoinek.roomrental.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-data-jpa-mongodb
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-01-17
 * Time: 05:47
 * To change this template use File | Settings | File Templates.
 */
@Document
public class Rooms {
    @Id
    public String id;

    public String name;
    public String thumbnailUrl;
    public Integer attendees;
    public Boolean videoProjector;
    public Boolean whiteboard;
    public Boolean handicapAccess;

    // Constructors
    public Rooms() {
    }

    public Rooms(String id, String name, String thumbnailUrl, Integer attendees, Boolean videoProjector, Boolean whiteboard, Boolean handicapAccess) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.attendees = attendees;
        this.videoProjector = videoProjector;
        this.whiteboard = whiteboard;
        this.handicapAccess = handicapAccess;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getAttendees() {
        return attendees;
    }

    public void setAttendees(Integer attendees) {
        this.attendees = attendees;
    }

    public Boolean getVideoProjector() {
        return videoProjector;
    }

    public void setVideoProjector(Boolean videoProjector) {
        this.videoProjector = videoProjector;
    }

    public Boolean getWhiteboard() {
        return whiteboard;
    }

    public void setWhiteboard(Boolean whiteboard) {
        this.whiteboard = whiteboard;
    }

    public Boolean getHandicapAccess() {
        return handicapAccess;
    }

    public void setHandicapAccess(Boolean handicapAccess) {
        this.handicapAccess = handicapAccess;
    }
}
