package com.mobiloitte.content.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.mobiloitte.content.dao.BlogDao;
import com.mobiloitte.content.dao.ContactUsDetailDao;
import com.mobiloitte.content.dao.FaqDao;
import com.mobiloitte.content.dao.FaqDataDao;
import com.mobiloitte.content.dto.BlogDto;
import com.mobiloitte.content.dto.FAQDto;
import com.mobiloitte.content.entities.Blog;
import com.mobiloitte.content.entities.ContactUs;
import com.mobiloitte.content.entities.FAQ;
import com.mobiloitte.content.entities.FaqData;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.FaqAndBlogService;

@Service
public class FaqAndBlogServiceImpl implements FaqAndBlogService {

	@Autowired
	private FaqDao faqDao;
	@Autowired
	private BlogDao blogDao;
	@Autowired
	private ContactUsDetailDao contactUsDetailDao;

	@Autowired
	private FaqDataDao faqDataDao;

	@Override
	public Response<Object> addNewFAQ(FAQDto fAQDto) {
		if (fAQDto.getQuestion() != null && fAQDto.getAnswer() != null && fAQDto.getTopicKey() != null) {

			Optional<FAQ> isFaqExisted = faqDao.findByTopicKey(fAQDto.getTopicKey());
			String newStr = fAQDto.getTopicKey().replaceAll("_", " ");

			if (isFaqExisted.isPresent()) {
				FaqData faq = new FaqData();
				faq.setTopicKey(fAQDto.getTopicKey());
				faq.setTopicName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newStr));
				faq.setAnswer(fAQDto.getAnswer());
				faq.setQuestion(fAQDto.getQuestion());
				faq.setIsDeleted(Boolean.FALSE);
				faqDataDao.save(faq);
			} else {
				FAQ faqObj = new FAQ();
				faqObj.setTopicKey(fAQDto.getTopicKey());
				faqObj.setTopicName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newStr));

				FAQ afterSave = faqDao.save(faqObj);

				FaqData faq = new FaqData();
				faq.setTopicKey(fAQDto.getTopicKey());
				faq.setTopicName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newStr));
				faq.setAnswer(fAQDto.getAnswer());
				faq.setQuestion(fAQDto.getQuestion());
				faq.setIsDeleted(Boolean.FALSE);
				faq.setFkFaqId(afterSave.getFaqId());
				faqDataDao.save(faq);

			}

			return new Response<>(200, "New Faq Details Add successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}

	}

	@Override
	public Response<Object> getFAQByTopicName(String topicName, Integer page, Integer pageSize) {
		Map<String, Object> response = new HashMap<>();
		Optional<FAQ> isFaqExisted = faqDao.findByTopicKey(topicName);
		if (isFaqExisted.isPresent()) {
			List<FaqData> faqList = faqDataDao.findByFkFaqIdOrderByFaqDataIdAsc(isFaqExisted.get().getFaqId());
			List<FaqData> data = faqDataDao.findByTopicKeyOrderByFaqDataIdAsc(isFaqExisted.get().getTopicKey(),
					PageRequest.of(page, pageSize));
			if (data.isEmpty()) {
				return new Response<>(205, "Data not found");
			} else {
				response.put("count", faqList.size());
				response.put("list", data);
				return new Response<>(200, "Data Found Successfully", response);
			}
		}

		return new Response<>(200, "Data Not Successfully");

	}

	@Override
	public Response<Object> getOnlyTopicOfFaq() {
		List<Map<String, String>> finalResponse = new LinkedList<>();
		List<FAQ> data = faqDao.findAll();
		Set<FAQ> uniqueFAQSet = data.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FAQ::getTopicKey))));
		if (!data.isEmpty()) {
			for (FAQ topicName : uniqueFAQSet) {
				Map<String, String> response = new HashMap<>();
				response.put("topic_key", topicName.getTopicKey());
				response.put("topic_name", topicName.getTopicName());
				finalResponse.add(response);
			}
			return new Response<>(200, "Data Found Successfully", finalResponse);

		} else {
			return new Response<>(205, "Data not found");
		}
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Override
	public Response<Object> deleteFAQ(Long faqId) {
		if (faqId != null) {
			Optional<FaqData> data = faqDataDao.findByFaqDataId(faqId);
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FaqData faq = data.get();
			faq.setIsDeleted(Boolean.TRUE);
			faqDataDao.save(faq);
			return new Response<>(200, " Faq Details Deleted successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> updateFAQ(FAQDto fAQDto) {
		if (fAQDto.getFaqId() != null) {
			Optional<FaqData> data = faqDataDao.findByFaqDataId(fAQDto.getFaqId());
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FaqData faq = data.get();
			if (fAQDto.getAnswer() != null)
				faq.setAnswer(fAQDto.getAnswer());
			if (fAQDto.getQuestion() != null)
				faq.setQuestion(fAQDto.getQuestion());
			faq.setIsDeleted(Boolean.FALSE);

			faqDataDao.save(faq);
			return new Response<>(200, "Faq Details Update successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> getFAQById(Long faqId) {
		if (faqId != null) {
			Optional<FaqData> data = faqDataDao.findByFaqDataId(faqId);
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FAQDto FAQDto = new FAQDto();
			FAQDto.setAnswer(data.get().getAnswer());
			FAQDto.setQuestion(data.get().getQuestion());
			FAQDto.setIsDeleted(data.get().getIsDeleted());
			FAQDto.setFaqId(data.get().getFkFaqId());
			FAQDto.setTopicKey(data.get().getTopicKey());
			FAQDto.setTopicName(data.get().getTopicName());
			return new Response<>(200, "Faq Details Update successfully", FAQDto);
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> getFAQList(Long userId, Integer page, Integer pageSize, String topicKey) {
		Map<String, Object> response = new HashMap<>();
		Optional<FAQ> isFaqExisted = faqDao.findByTopicKey(topicKey);
		List<FaqData> faqList = faqDataDao.findByTopicKey(topicKey);

		if (isFaqExisted.isPresent()) {

			List<FaqData> data = faqDataDao.findByFkFaqIdOrderByFkFaqIdAsc(isFaqExisted.get().getFaqId(),
					PageRequest.of(page, pageSize));
			if (data.isEmpty()) {
				return new Response<>(205, "Data not found");
			} else {
				response.put("count", faqList.size());
				response.put("list", data);

				return new Response<>(200, "Data Found Successfully", response);
			}
		}

		return new Response<>(205, "Data not found");

	}

	@Override
	public Response<Object> getFAQByIdForWebsite(Long faqId) {
		if (faqId != null) {
			Optional<FaqData> data = faqDataDao.findByFkFaqId(faqId);
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FAQDto FAQDto = new FAQDto();
			FAQDto.setAnswer(data.get().getAnswer());
			FAQDto.setQuestion(data.get().getQuestion());
			FAQDto.setIsDeleted(data.get().getIsDeleted());
			FAQDto.setFaqId(data.get().getFkFaqId());
			return new Response<>(200, "Faq Details Update successfully", FAQDto);
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> getFAQListForWebsite(Integer page, Integer pageSize, String topicKey) {
		Map<String, Object> response = new HashMap<>();
		Optional<FAQ> isFaqExisted = faqDao.findByTopicKey(topicKey);
		List<FaqData> faqList = faqDataDao.findByTopicKey(topicKey);
		if (isFaqExisted.isPresent()) {

			List<FaqData> data = faqDataDao.findByFkFaqIdOrderByFkFaqIdAsc(isFaqExisted.get().getFaqId(),
					PageRequest.of(page, pageSize));
			if (data.isEmpty()) {
				return new Response<>(205, "Data not found");
			} else {
				response.put("count", faqList.size());
				response.put("list", data);

				return new Response<>(200, "Data Found Successfully", response);
			}
		}

		return new Response<>(205, "Data not found");

	}

	@Override
	public Response<Object> addNewBlog(BlogDto blogDto) {

		Blog blog = new Blog();
		blog.setAuthor(blogDto.getAuthor());
		blog.setCreatedAt(new Date());
		blog.setUpdatedAt(new Date());
		blog.setImage(blogDto.getImage());
		blog.setDescription(blogDto.getDescription());
		blog.setIsDeleted(Boolean.FALSE);
		blog.setTitle(blogDto.getTitle());
		blog.setRecommended(blogDto.getRecommended());
		blogDao.save(blog);

		return new Response<>(200, "Data save successfully");
	}

	@Override
	public Response<Object> deleteBlog(Long blogId) {
		if (blogId != null) {
			Optional<Blog> data = blogDao.findById(blogId);
			if (!data.isPresent()) {
				return new Response<>(205, " Detail Not Present.");
			}
			Blog blog = data.get();

			blog.setIsDeleted(Boolean.TRUE);

			blog.setUpdatedAt(new Date());
			blogDao.save(blog);
			return new Response<>(200, "Details Deleted successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> updateBlog(BlogDto blogDto) {
		if (blogDto.getContentId() != null) {
			Optional<Blog> data = blogDao.findById(blogDto.getContentId());
			if (!data.isPresent()) {
				return new Response<>(205, "Details not present");
			}
			Blog blog = data.get();
			if (blogDto.getAuthor() != null)
				blog.setAuthor(blogDto.getAuthor());

			blog.setUpdatedAt(new Date());
			if (blogDto.getImage() != null)
				blog.setImage(blogDto.getImage());
			if (blogDto.getDescription() != null)
				blog.setDescription(blogDto.getDescription());
			blog.setTitle(blogDto.getTitle());
			blog.setIsDeleted(Boolean.FALSE);
			blog.setRecommended(blogDto.getRecommended());
			blogDao.save(blog);

			return new Response<>(200, "Data save successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> getBlogById(Long blogId) {
		Optional<Blog> data = blogDao.findById(blogId);
		if (!data.isPresent()) {
			return new Response<>(205, "Details not present");
		} else {
			return new Response<>(200, "Details Found", data);
		}
	}

	@Override
	public Response<Object> getBlogList(Long userId, Integer page, Integer pageSize) {
		Map<String, Object> response = new HashMap<>();
		List<Blog> blogList = blogDao.findAll();

		List<Blog> data = blogDao.findByOrderByCreatedAtAsc(PageRequest.of(page, pageSize));
		if (data.isEmpty()) {
			return new Response<>(205, "Data not found");
		} else {
			response.put("count", blogList.size());
			response.put("list", data);
			return new Response<>(200, "Data Found Successfully", response);
		}
	}

	@Override
	public Response<Object> getBlogByIdForWebsite(Long blogId) {
		Optional<Blog> data = blogDao.findById(blogId);
		if (!data.isPresent()) {
			return new Response<>(205, "Details not present");
		} else {
			return new Response<>(200, "Details Found", data);
		}
	}

	@Override
	public Response<Object> getBlogListForWebsite(Integer page, Integer pageSize, String title) {
		if(title==null) {
		List<Blog> data = blogDao.findByOrderByCreatedAtAsc(PageRequest.of(page, pageSize));
		Map<String, Object> map = new HashMap<>();
		List<Blog> count = blogDao.findAll();
		map.put("Count", count.size());
		map.put("List", data);
		if (data.isEmpty()) {

			return new Response<>(205, "Data not found");
		} else {
			return new Response<>(200, "Data Found Successfully", map);
		}
		}if(title!=null) {
			List<Blog> data = blogDao.findByTitleContainsOrderByCreatedAtAsc(title,PageRequest.of(page, pageSize));
			Map<String, Object> map = new HashMap<>();
			List<Blog> count = blogDao.findAll();
			map.put("Count", count.size());
			map.put("List", data);
			if (data.isEmpty()) {

				return new Response<>(205, "Data not found");
			} else {
				return new Response<>(200, "Data Found Successfully", map);
			}
		}
		return new Response<>(205, "Data not found");
	}

	@Override
	public Response<Object> addContactDetails(List<String> email, List<String> mobileNo) {

		if (!email.isEmpty()) {
			for (String s : email) {
				ContactUs contactDetails = new ContactUs();
				contactDetails.setEmail(s);
				contactDetails.setIsDeleted(Boolean.FALSE);
				contactUsDetailDao.save(contactDetails);
			}
		}

		if (!mobileNo.isEmpty()) {
			for (String s : mobileNo) {
				ContactUs contactDetails = new ContactUs();
				contactDetails.setPhoneNo(s);
				contactDetails.setIsDeleted(Boolean.FALSE);
				contactUsDetailDao.save(contactDetails);
			}
		}
		return new Response<>(200, "Data save successfully");
	}

	@Override
	public Response<Object> getContactDetails() {
		List<ContactUs> data = contactUsDetailDao.findAll();
		if (data.isEmpty())
			return new Response<>(205, "Data not found");
		else
			return new Response<>(200, "Data found", data);

	}

	@Override
	public Response<Object> updateContactDetails(Long id, String data, String isDelete) {
		if (id != null && isDelete != null) {
			Optional<ContactUs> detailsForDelete = contactUsDetailDao.findById(id);

			if (detailsForDelete.isPresent()) {

				detailsForDelete.get().setIsDeleted(Boolean.TRUE);
				contactUsDetailDao.save(detailsForDelete.get());
				return new Response<>(200, "Data update successfully");
			} else {
				return new Response<>(205, "Data not found");
			}
		} else {
			Optional<ContactUs> details = contactUsDetailDao.findById(id);
			if (details.isPresent()) {
				if (details.get().getEmail() != null)
					details.get().setEmail(data);
				if (details.get().getPhoneNo() != null)
					details.get().setPhoneNo(data);
				contactUsDetailDao.save(details.get());
				return new Response<>(200, "Data update successfully");
			} else {
				return new Response<>(205, "Data not found");
			}
		}
	}

	@Override
	public Response<Object> getFAQByTopicNameWebsite(String topicName, Object object, Object object2) {
		List<FaqData> data = faqDataDao.findByTopicKey(topicName);
		if (data.isEmpty()) {
			return new Response<>(205, "Data not found");
		} else {
			return new Response<>(200, "Data Found Successfully", data);
		}
	}

	@Override
	public Response<Object> getFaqList(String question, Integer page, Integer pageSize) {
		Page<FaqData> isletterExists = faqDataDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, "faqDataId"));

		if (!isletterExists.isEmpty()) {
			List<FaqData> letterExist = faqDataDao.findByQuestion(question,
					PageRequest.of(page, pageSize, Direction.DESC, "faqDataId"));
			if (!letterExist.isEmpty()) {
				return new Response<>(200, "List fetched", letterExist);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("Count", isletterExists.getSize());
			map.put("List", isletterExists);
			return new Response<>(200, "All Data Found", map);
		} else {
			return new Response<>(205, "No Data Found");
		}
	}

}
