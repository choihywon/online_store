package com.example.bookstore.user.service;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.domain.UserRole;
import com.example.bookstore.user.dto.JoinUserDto;
import com.example.bookstore.user.dto.LoginUserDto;
import com.example.bookstore.user.dto.UpdateUserDto;
import com.example.bookstore.user.dto.UserDto;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto joinUser(JoinUserDto joinUserDto) {
        System.out.println("회원가입 요청 데이터: email=" + joinUserDto.getEmail() + ", password=" + joinUserDto.getPassword());

        if (joinUserDto.getPassword() == null || joinUserDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호 입력 해야합니다.");
        }

        User user = User.builder()
                .email(joinUserDto.getEmail())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))  //비밀번호 암호화
                .phone(joinUserDto.getPhone())
                .nickname(joinUserDto.getNickname())
                .grade("일반")
                .mileage(0)
                .useYn('Y')
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        System.out.println("✅ 회원가입 완료: " + user.getEmail());
        return new UserDto(user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.getUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt());
    }


    public UserDto loginUser(LoginUserDto loginUserDto) {
        System.out.println("로그인 요청: email=" + loginUserDto.getEmail());

        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        System.out.println("사용자 조회 성공: " + user.getEmail());

        boolean passwordMatches = passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword());
        System.out.println("비밀번호 일치 여부: " + passwordMatches);

        if (!passwordMatches) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        System.out.println("로그인 성공: " + user.getEmail());

        return new UserDto(
                user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.getUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt()
        );
    }




    //이메일 중복 확인
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //회원 정보 조회 (이메일 기준)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        return new UserDto(
                user.getEmail(), user.getPhone(), user.getNickname(),
                user.getGrade(), user.getMileage(), user.getUseYn(),
                user.getCreatedAt(), user.getLastModifiedAt()
        );
    }

    //회원 정보 업데이트 (닉네임 & 전화번호 변경)
    @Transactional
    public void updateUser(Long userSeq, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(updateUserDto.getPhone(), updateUserDto.getNickname());
    }

    //회원 탈퇴 (삭제)
    @Transactional
    public void deleteUser(Long userSeq) {
        userRepository.deleteById(userSeq);
    }
}