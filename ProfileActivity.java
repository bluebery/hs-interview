package com.circle.profile.ProfileActivity;
import com.hootsuite.manager.Manager;
import com.circle.somelibrary.*;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import butterknife.InjectView;
import kotlin.Pair;

public final class ProfileListFragment extends Fragment implements SomeInterface {
    
    @Inject
    private ManagerImpl manager;
    private TextView mCountText;  
  
    protected LinkedList<SocialProfile> socialProfileList = new LinkedList<SocialProfile>();
    Activity mParentActivity;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = activity;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socialProfileList.add(getIntent().getExtras().getParcelable("profileList"));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState)
        View view = inflater.inflate(R.id.profile_screen, container, false);
        return view;
    }
    
    public void onStart() {
        mCountText = (TextView) findViewById(R.id.text_count);
        RecyclerView recyclingView = (RecyclerView) findViewById(R.id.recycler_view);
        
        mCountText.setText("Count of Profiles " + socialProfiles.size());
        recyclingView.setAdapter(new SocialProfilesAdapter(socialProfileList));
        recyclingView.setLayoutManager(new LinearLayoutManager());
    }
        
    /**
     * Finds and returns the profiles in this fragment that is similar to the similarTo parameter.
     * @param similarTo A collection of profiles to be similar to
     */
    public LinkedList<SocialProfile> similar(ArrayList<SocialProfile> similarTo) {
        for (int i = 0; i < socialProfileList.size() - 1; i++) {
            SocialProfile currentProfile = socialProfileList.get(i);
            for (int j = 0; j < similarTo.size() - 1; j++) {
                SocialProfile similarProfile = similarTo.get(j);
                if (compareInterests(currentProfile, similarProfile) == 0) {
                    socialProfileList.remove(i)
                }
            }
        }
        return socialProfileList;
    }

    public int compareInterests(SocialProfile s1, SocialProfile s2) {
        int count = 0;
        for (Pair<Integer, Interests> interestRank : s1.interestsRank) {
            for(Pair<Integer, Interests> interestRank2 : s2.interestsRank) {
                if (interestRank2.getSecond() == interestRank.getSecond()) {
                    if (interestRank.getFirst() != interestRank.getFirst()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    class SocialProfile implements Parcelable {
        // Parcelable Implementation Omitted
        // A List of Interests and their rank
        // Example: [{1, "MUSIC"}, {2, "FOOD"}, ... ]
        public List<Pair<Integer, Interests>> interestsRank;
    }

    private enum Interests {
        MUSIC,
        SPORTS,
        SCIENCE,
        MOVIES,
        FOOD
    }
 
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_compare:
                mCountText.setText(compareInterests(socialProfileList.get(0), socialProfileList.get(1)));
        }
        return super.onOptionsItemSelected(item);
    }
}