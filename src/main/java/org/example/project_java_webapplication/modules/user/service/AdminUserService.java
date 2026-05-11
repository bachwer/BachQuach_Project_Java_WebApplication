package org.example.project_java_webapplication.modules.user.service;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.Role;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.entity.UserProfiles;
import org.example.project_java_webapplication.modules.auth.repository.RoleRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserProfilesRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.user.dto.StudentUpdateDTO;
import org.example.project_java_webapplication.modules.user.repository.DepartmentRepository;
import org.example.project_java_webapplication.modules.user.dto.LecturerCreateDTO;
import org.example.project_java_webapplication.modules.user.dto.LecturerUpdateDTO;
import org.example.project_java_webapplication.modules.user.dto.StudentCreateDTO;
import org.example.project_java_webapplication.modules.user.entity.Department;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.user.entity.Student;
import org.example.project_java_webapplication.modules.user.repository.LecturerRepository;
import org.example.project_java_webapplication.modules.user.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserProfilesRepository userProfilesRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Lecturer createLecturer(LecturerCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName("LECTURER")
                .orElseThrow(() -> new RuntimeException("Role LECTURER not found"));

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isActive(true)
                .roles(Set.of(role))
                .build();
        user = userRepository.save(user);

        UserProfiles profile = new UserProfiles();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setAvatarUrl(dto.getAvatarUrl());
        userProfilesRepository.save(profile);

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Lecturer lecturer = Lecturer.builder()
                .user(user)
                .department(department)
                .academicRank(dto.getAcademicRank())
                .specialization(dto.getSpecialization())
                .build();

        return lecturerRepository.save(lecturer);
    }

    @Transactional
    public Lecturer updateLecturer(Long lecturerId, LecturerUpdateDTO dto) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        User user = lecturer.getUser();

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userRepository.save(user);

        UserProfiles profile = userProfilesRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User profile not found!"));

        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setAvatarUrl(dto.getAvatarUrl());
        userProfilesRepository.save(profile);

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found!"));
        lecturer.setDepartment(department);
        lecturer.setAcademicRank(dto.getAcademicRank());
        lecturer.setSpecialization(dto.getSpecialization());

        return lecturerRepository.save(lecturer);
    }

    @Transactional
    public Student updateStudent(Long userId, StudentUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userRepository.save(user);

        UserProfiles profile = userProfilesRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User profile not found!"));

        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setAvatarUrl(dto.getAvatarUrl());
        userProfilesRepository.save(profile);

        Student student = studentRepository.findByUser(user).orElseGet(() -> {
            return Student.builder().user(user).build();
        });

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found!"));
            student.setDepartment(department);
        }

        // Auto-generate student code during update if still missing or NOT SET
        String studentCode = dto.getStudentCode();
        if (studentCode == null || studentCode.trim().isEmpty() || studentCode.equals("NOT SET")) {
            // Only auto-gen if not already set or if explicitly requested via empty input
            if (student.getStudentCode() == null || student.getStudentCode().equals("NOT SET")) {
                studentCode = generateNextStudentCode();
            } else {
                studentCode = student.getStudentCode(); // Keep existing
            }
        }
        student.setStudentCode(studentCode);

        return studentRepository.save(student);
    }



    @Transactional
    public Student createStudent(StudentCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isActive(true)
                .roles(Set.of(role))
                .build();
        user = userRepository.save(user);

        UserProfiles profile = new UserProfiles();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setAvatarUrl(dto.getAvatarUrl());
        userProfilesRepository.save(profile);

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Auto-generate student code if not provided or requested
        String studentCode = dto.getStudentCode();
        if (studentCode == null || studentCode.trim().isEmpty() || studentCode.equals("NOT SET")) {
            studentCode = generateNextStudentCode();
        }

        Student student = Student.builder()
                .user(user)
                .department(department)
                .studentCode(studentCode)
                .build();

        return studentRepository.save(student);
    }

    private String generateNextStudentCode() {
        String maxCode = studentRepository.findMaxStudentCode();
        if (maxCode == null) {
            return "STU-001";
        }
        try {
            // Assumes format STU-XXX
            int lastNum = Integer.parseInt(maxCode.substring(4));
            return String.format("STU-%03d", lastNum + 1);
        } catch (Exception e) {
            // Fallback if maxCode is not in expected format
            return "STU-" + System.currentTimeMillis() % 1000;
        }
    }

    public List<User> getAllStudents() {
        return userRepository.findByRolesName("STUDENT");
    }

    public List<User> searchStudents(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllStudents();
        }
        return userRepository.findByRolesNameAndProfileFullNameContainingIgnoreCase("STUDENT", name);
    }

    public List<User> getAllLecturers() {
        return userRepository.findByRolesName("LECTURER");
    }

    public List<User> searchLecturers(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllLecturers();
        }
        return userRepository.findByRolesNameAndProfileFullNameContainingIgnoreCase("LECTURER", name);
    }

    public List<Lecturer> getAllLecturerEntities() {
        return lecturerRepository.findAll();
    }

    public List<Lecturer> searchLecturerEntities(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllLecturerEntities();
        }
        return lecturerRepository.findByUserProfileFullNameContainingIgnoreCase(name);
    }

    public User getStudentById(Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("STUDENT")))
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
