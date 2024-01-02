    package org.example.receiver.controller;

    import com.baomidou.mybatisplus.core.toolkit.StringUtils;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import org.bson.types.ObjectId;
    import org.example.receiver.dto.CustomPage;
    import org.example.receiver.entity.Parcel;
    import org.example.receiver.entity.ParcelTrack;
    import org.example.receiver.repository.ParcelRepository;
    import org.example.receiver.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.mongodb.core.MongoTemplate;
    import org.springframework.data.mongodb.core.query.Criteria;
    import org.springframework.data.mongodb.core.query.Query;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.List;

@RestController
@RequestMapping("/Receiver")
public class ReceiverController {
    private final ParcelRepository parcelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    public ReceiverController(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }
    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get parce traces", description = "Allowed User gets one parcel tracks")
    @GetMapping("/getHistory")
    public List<ParcelTrack> getHistory(@RequestParam String uuid) {
        Parcel parcel = parcelRepository.findById(uuid).orElse(null);;
        if(parcel == null){return null;}
        return parcel.getTracks();
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get a parcelList", description = "Allowed User gets their parcels")
    @GetMapping("/getParcelList")
    public CustomPage getParcelList(@RequestParam int receiverID, int pageNo, String lastId) {

        final Criteria criteriaDefinition = Criteria.where("student").is(receiverID);
        final Query query = new Query(criteriaDefinition).with(Sort.by(Sort.Order.desc("_id")));
        long total = mongoTemplate.count(query, Parcel.class);
        int pageSize = 10;
        int pages = (int) Math.ceil(total / (double) pageSize);
        if (pageNo<=0 || pageNo> pages) {
            pageNo = 1;
        }

        if (StringUtils.isNotBlank(lastId)) {
            if (pageNo != 1) {
                criteriaDefinition.and("_id").lt(new ObjectId(lastId));
            }
            query.limit(pageSize);
        } else {
            int skip = pageSize * (pageNo - 1);
            query.skip(skip).limit(pageSize);
        }
        List<Parcel> newList = mongoTemplate.find(query, Parcel.class);
        return new CustomPage(newList, total, pageSize, pageNo, pages);



//        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "tracks.create_at");
//        AggregationOperation unwind = Aggregation.unwind("tracks");
//        AggregationOperation secondSort = Aggregation.sort(Sort.Direction.DESC, "latestTrack.create_at");
//        AggregationOperation matchid = Aggregation.match(Criteria.where("parcel.student").is(receiverID));
//        AggregationOperation group = Aggregation.group("_id")
//                .first("type").as("type")
//                .first("address1").as("address1")
//                .first("address2").as("address2")
//                .first("student").as("student")
//                .first("tracks").as("latestTrack");
//        Aggregation aggregation = newAggregation(matchid, unwind, sort, group, secondSort);
//        AggregationResults<ParcelWithLatestTrack> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelWithLatestTrack.class);
//        List<ParcelWithLatestTrack> parcels = results.getMappedResults();
//        int total = parcels.size();
//        int pageSize = 10;
//        int fromIndex = Math.min((pageNo - 1) * pageSize, total);
//        int toIndex = Math.min(fromIndex + pageSize, total);
//        List<org.example.receiver.dto.ParcelWithLatestTrack> list = parcels.subList(fromIndex, toIndex);
//        List<ParcelWithStudentInfo> newList = new ArrayList<>();
//        for(org.example.receiver.dto.ParcelWithLatestTrack p : list) {
//            User student = userService.getStudentById(p.getStudent());
//            com.example.estate.entity.ParcelTrack latestTrack = p.getLatestTrack();
//            ParcelWithStudentInfo parcelWithStudentInfo = new ParcelWithStudentInfo(
//                    p.getId(), new StudentInfo(student.getUsername(), student.getEmail()),
//                    p.getType(), p.getAddress1(), p.getAddress2(), latestTrack.getDescription(), latestTrack.getCreate_at());
//            newList.add(parcelWithStudentInfo);
//        }
//        long pages = (long) Math.ceil((double) total / pageSize);
//        return new CustomPage(list, total, pageSize, pageNo, pages);

    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get a parcelList", description = "Allowed User gets their parcels")
    @PostMapping("/confirmed")
    public boolean confirmed(@RequestParam int receiverID, String uuid) {
        Parcel parcel = parcelRepository.findById(uuid).orElse(null);;
        if(parcel != null){
            List<ParcelTrack> parcelTracks = parcel.getTracks();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            ParcelTrack parcelTrack = new ParcelTrack("Confirmed", receiverID, formattedDateTime);
            parcelTracks.add(parcelTrack);
            parcelRepository.save(parcel);
            return true;
        }
        return false;
    }
}
