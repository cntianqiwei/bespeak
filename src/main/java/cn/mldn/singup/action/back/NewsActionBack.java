package cn.mldn.singup.action.back;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.mldn.singup.service.back.INewsServiceBack;
import cn.mldn.singup.vo.News;
import cn.mldn.util.action.AbstractAction;
@Controller
@RequestMapping("/admin/news/*")
public class NewsActionBack extends AbstractAction {
	@Resource
	private INewsServiceBack newsServiceBack ;
	@RequestMapping("addPre")
	@RequiresRoles("news")
	@RequiresPermissions("news:add")
	public ModelAndView addPre() {
		ModelAndView mav = new ModelAndView(super.getValue("back.news.add.page")) ;
		mav.addAllObjects(this.newsServiceBack.addPre()) ;
		return mav ;
	}

	@RequestMapping("add")
	@RequiresRoles("news")
	@RequiresPermissions("news:add")
	public ModelAndView add(News vo,MultipartFile pic,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(super.getValue("forward.back.page")) ;
		if (pic != null) {
			String fileName = super.createFileName(pic) ;	// 需要创建一个名称
			vo.setMid(super.getMid()); 	// 设置进行新闻创建的管理员信息
			vo.setPhoto(fileName);	// 保存生成的图片名称
			if (this.newsServiceBack.add(vo)) {
				super.saveFile(pic, fileName, request) ;	// 图片保存
				super.setMsgAndUrl(mav, "vo.add.success.msg", "back.news.add.action");
			} else {
				super.setMsgAndUrl(mav, "vo.add.failure.msg", "back.news.add.action");
			}
		}
		return mav ; 
	}
	
	@RequestMapping("checkTitle")
	@RequiresRoles("news")
	@RequiresPermissions("news:add")
	public ModelAndView checkTitle(String title,HttpServletResponse response) {
		super.print(response, this.newsServiceBack.getByTitle(title) == null );
		return null ;
	}
	@Override
	public String getType() {
		return "公告";
	}
	@Override
	public String getFileUploadDir() {
		return "/upload/news/";
	}
}
