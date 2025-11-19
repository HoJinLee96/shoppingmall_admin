package net.chamman.shoppingmall_admin.view;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import net.chamman.shoppingmall_admin.security.principal.AdminDetails;

@Controller
@EnableMethodSecurity
@RequiredArgsConstructor
public class AdminMyPageViewController {

//	private final AdminService adminService;

	@GetMapping("/admin/my")
	public String showMy(@AuthenticationPrincipal AdminDetails adminDetails, Model model) {
		return "admin/my/my";
	}

//	@GetMapping("/admin/my/signInfo")
//	public String showMyLoginInfo(@AuthenticationPrincipal AdminDetails adminDetails, Model model) {
//
//		Admin admin = adminService.getActiveAdminById(adminDetails.getAdminId());
//		model.addAttribute("admin", AdminResponseDto.from(admin));
//
//		return "admin/my/mySignInfo";
//	}
//
//	@GetMapping("/admin/my/signInfo/password")
//	public String showMyPassword(@AuthenticationPrincipal AdminDetails adminDetails, Model model) {
//		Admin admin = adminService.getActiveAdminById(adminDetails.getAdminId());
//		model.addAttribute("admin", AdminResponseDto.from(admin));
//		return "admin/my/myPassword";
//	}
//
//	@GetMapping("/admin/my/signInfo/withdrawal")
//	public String showWithdrawal() {
//		return "admin/my/withdrawal";
//	}
//
//	@GetMapping("/admin/my/profile")
//	public String showMyProfile(@AuthenticationPrincipal AdminDetails adminDetails, Model model) {
//
//		Admin admin = adminService.getActiveAdminById(adminDetails.getAdminId());
//		model.addAttribute("admin", AdminResponseDto.from(admin));
//
//		return "admin/my/myProfile";
//	}

}
