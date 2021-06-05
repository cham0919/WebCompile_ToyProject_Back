package com.wcp.user;

import com.wcp.WCPTable.UserTable;
import com.wcp.board.Board;
import com.wcp.coding.room.CodingRoom;
import com.wcp.coding.submit.SubmitHistory;
import com.wcp.commant.BoardCommant;
import com.wcp.convert.UserRoleToValueConverter;
import com.wcp.convert.UserStatusToValueConverter;
import com.wcp.like.BoardLike;
import com.wcp.security.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = UserTable.TABLE_NAME)
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserTable.PK)
    private Long key;

    @Column(name = UserTable.ID, nullable = false, unique = true)
    private String id;

    @Column(name = UserTable.PW, nullable = false)
    private String password;

    @Column(name = UserTable.NAME, nullable = false)
    private String name;

    @Column(name = UserTable.NICKNAME, length = 100, unique = true)
    private String nickname;

    @Column(name = UserTable.PHONE, length = 100, unique = true)
    private String phone;

    @Column(name = UserTable.REGISTER_AT)
    @CreationTimestamp
    private LocalDateTime registerAt;

    @Column(name = UserTable.ROLE)
    @Convert(converter = UserRoleToValueConverter.class)
    @ColumnDefault("'ROLE_MEMBER'")
    private Role role;

    @Column(name = UserTable.STATUS)
    @Convert(converter = UserStatusToValueConverter.class)
    private UserSataus status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BoardCommant> boardCommants = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BoardLike> boardLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CodingRoom> codingRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SubmitHistory> submitHistories = new ArrayList<>();

    public User setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "key=" + key +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", register_at=" + registerAt +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
