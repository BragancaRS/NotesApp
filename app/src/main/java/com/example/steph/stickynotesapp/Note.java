package com.example.steph.stickynotesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by steph on 1/1/2017.
 */
public class Note implements Parcelable {
    private String title;
    private String Description;
    private String content;
    private Date CreateDate;
    private Date LastModified;

    /**
     * Gets locked.
     *
     * @return the locked
     */
    public int getLocked() {
        return locked;
    }

    /**
     * Sets locked.
     *
     * @param locked the locked
     */
    public void setLocked(int locked) {
        this.locked = locked;
    }

    private int locked;

    /**
     * Instantiates a new Note.
     */
    public Note() {
    }


    /**
     * Instantiates a new Note.
     *
     * @param title        the title
     * @param description  the description
     * @param content      the content
     * @param createDate   the create date
     * @param lastModified the last modified
     * @param locked       the locked
     */
    public Note(String title, String description, String content, Date createDate, Date lastModified, int locked) {
        this.title = title;
        Description = description;
        this.content = content;
        CreateDate = createDate;
        LastModified = lastModified;
        this.locked=locked;
    }

    /**
     * Instantiates a new Note.
     *
     * @param in the in
     */
    protected Note(Parcel in) {
        title = in.readString();
        Description = in.readString();
        content = in.readString();
        CreateDate = new Date(in.readLong());
        LastModified = new Date(in.readLong());
        locked=(in.readInt());
    }

    /**
     * The constant CREATOR.
     */
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public Date getCreateDate() {
        return CreateDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    /**
     * Gets last modified.
     *
     * @return the last modified
     */
    public Date getLastModified() {
        return LastModified;
    }

    /**
     * Sets last modified.
     *
     * @param lastModified the last modified
     */
    public void setLastModified(Date lastModified) {
        LastModified = lastModified;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(Description);
        dest.writeString(content);
        dest.writeLong(CreateDate.getTime());
        dest.writeLong(LastModified.getTime());
        dest.writeInt(locked);

    }
}


